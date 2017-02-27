package com.design.service.impl.refund;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderState;
import com.design.common.enums.RefundState;
import com.design.common.utils.DateUtil;
import com.design.dao.entity.Order;
import com.design.dao.entity.Refund;
import com.design.dao.entity.RefundDetail;
import com.design.dao.entity.Trade;
import com.design.dao.persist.RefundDetailMapper;
import com.design.service.api.IThirdTradeService;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.TradeMoneyRemainDto;
import com.design.service.api.dto.resp.TradeResp;
import com.google.common.base.Throwables;
@Service
public class PreSellRefundDetailServiceImpl implements IRefundDetailService{
	private static Logger log = LoggerFactory.getLogger(PreSellRefundDetailServiceImpl.class);
	
	private Map<String,IThirdTradeService> thridTradeServices;
	@Resource
	private RefundDetailMapper refundDetailMapper;
	@Resource
	private RefundServiceImpl refundServiceImpl;
	
	@Override
	public void dealRefund(RefundDto refundDto){
		log.info("预售退款处理开始...");
		Refund refund = refundDto.getRefund();
		BigDecimal expectMoney = refundDto.getRefundMoney();
		Order order = refundDto.getOrder();
		if(OrderState.PRE_PAY_SUCCESS.getStateCode().equals(order.getOrderState())){
			//已付定金状态,执行第一次支付对应的退款
			TradeMoneyRemainDto preOrderTmrDto = refundServiceImpl.getTradeMoneyReamin(order.getPreOrderNo(), refundDto.getRefundNo());
			BigDecimal totalRemain = preOrderTmrDto.getRemainMoney();
			if(totalRemain.compareTo(expectMoney)==-1){
				log.info("退款总额超出付款金额,期望退款总额{},支付剩余总额{}",expectMoney,totalRemain);
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
			try {
				doOneRefund(refund, preOrderTmrDto,refund.getRefundNo()+"A",expectMoney);
			} catch (Exception e) {
				log.info("异常信息:{}",Throwables.getStackTraceAsString(e));
			}
			return;
		}
		
		TradeMoneyRemainDto tmrDto = refundServiceImpl.getTradeMoneyReamin(order.getOrderNo(), refundDto.getRefundNo());
		if(tmrDto.getRemainMoney().compareTo(expectMoney)==-1){
			//第二次支付剩余金额不足
			log.info("退款总额超出订单第二次支付剩余金额,期望退款总额{},该订单第二次支付剩余总额{}",expectMoney,tmrDto.getRemainMoney());
			TradeMoneyRemainDto preOrderTmrDto = refundServiceImpl.getTradeMoneyReamin(order.getPreOrderNo(), refundDto.getRefundNo());
			BigDecimal totalRemain = tmrDto.getRemainMoney().add(preOrderTmrDto.getRemainMoney());
			if(totalRemain.compareTo(expectMoney)==-1){
				log.info("退款总额超出付款金额,期望退款总额{},支付剩余总额{}",expectMoney,totalRemain);
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
			StringBuilder builder = new StringBuilder();
			if(tmrDto.getRemainMoney().compareTo(BigDecimal.ZERO)==1){
				try {
					log.info("执行第二次支付对应的退款");
					doOneRefund(refund, preOrderTmrDto,refund.getRefundNo()+"B",tmrDto.getRemainMoney());
				} catch (Exception e) {
					log.info("异常信息:{}",Throwables.getStackTraceAsString(e));
					builder.append(Throwables.getStackTraceAsString(e));
				}
				try {
					log.info("执行第一次支付对应的退款");
					doOneRefund(refund, preOrderTmrDto,refund.getRefundNo()+"A",expectMoney.subtract(preOrderTmrDto.getRemainMoney()));
				} catch (Exception e) {
					log.info("异常信息:{}",Throwables.getStackTraceAsString(e));
					builder.append(Throwables.getStackTraceAsString(e));
				}
			}else{
				try {
					log.info("执行第一次支付对应的退款");
					doOneRefund(refund, preOrderTmrDto,refund.getRefundNo()+"A",expectMoney);
				} catch (Exception e) {
					log.info("异常信息:{}",Throwables.getStackTraceAsString(e));
					builder.append(Throwables.getStackTraceAsString(e));
				}
			}
			if(builder.length()>0){
				throw new DesignException(builder.toString());
			}
		}else{
			try {
				//执行第二次支付的退款
				doOneRefund(refund, tmrDto,refund.getRefundNo()+"B",expectMoney);
			} catch (Exception e) {
				log.info("异常信息:{}",Throwables.getStackTraceAsString(e));
				throw new DesignException(Throwables.getStackTraceAsString(e));
			}
		}
	}

	private void doOneRefund(Refund refund, TradeMoneyRemainDto preOrderTmrDto,String refundDetailNo,BigDecimal refundMoney)
			throws Exception {
		Trade trade = preOrderTmrDto.getTrade();
		RefundDetail refundDetail = new RefundDetail();
		refundDetail.setRefundNo(refund.getRefundNo());
		refundDetail.setRefundDetailNo(refundDetailNo);
		refundDetail.setRefundState(RefundState.REFUNDING.getStateCode());
		refundDetail.setPayTypeId(trade.getPayTypeId());
		refundDetail.setCurrencyId(trade.getCurrencyId());
		refundDetail.setTradeNo(trade.getTradeNo());
		refundDetail.setRefundDetailMoney(refundMoney);
		refundDetail.setCreateTime(DateUtil.getCurrentDate());
		//执行第一次支付对应的退款
		doRefund(trade,refundDetail);
	}

	private void doRefund(Trade trade,RefundDetail refundDetail) throws Exception {
		refundDetailMapper.insert(refundDetail);
		int guard = -99;
		try{
			log.info("拼接第三方请求");
			BaseRefundDto baseRefundDto = new BaseRefundDto();
			baseRefundDto.setOrderNo(trade.getOrderNo());
			baseRefundDto.setRefundNo(refundDetail.getRefundDetailNo());
			baseRefundDto.setTradeNo(refundDetail.getTradeNo());
			baseRefundDto.setRefundMoney(refundDetail.getRefundDetailMoney());
			baseRefundDto.setPayMoney(trade.getPayMoney());
			IThirdTradeService thirdTradeService = thridTradeServices.get(trade.getPayTypeId());
			if(thirdTradeService==null){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
			log.info("请求第三方支付执行退款,{}",baseRefundDto);
			TradeResp tradeResp = thirdTradeService.tradeRefund(baseRefundDto);
			if(tradeResp.isSuccess()){
				guard = 1;
			}else{
				log.info("调用第三方退款返回失败");
				guard = -2;
				throw new DesignException(tradeResp.getSubMsg());
			}
		}catch(Exception e){
			log.info("调用第三方退款异常");
			refundDetail.setRefundState(RefundState.REFUND_FAIL.getStateCode());
			throw e;
		}finally{
			log.info("guard值：{}",guard);
			refundDetail.setUpdateTime(DateUtil.getCurrentDate());
			refundDetailMapper.update(refundDetail);
		}
	}

	public Map<String, IThirdTradeService> getThridTradeServices() {
		return thridTradeServices;
	}

	public void setThridTradeServices(
			Map<String, IThirdTradeService> thridTradeServices) {
		this.thridTradeServices = thridTradeServices;
	}
	
}

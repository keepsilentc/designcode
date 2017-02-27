package com.design.service.impl.refund;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.RefundState;
import com.design.common.utils.DateUtil;
import com.design.dao.entity.Refund;
import com.design.dao.entity.RefundDetail;
import com.design.dao.entity.Trade;
import com.design.dao.persist.RefundDetailMapper;
import com.design.service.api.IThirdTradeService;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.TradeMoneyRemainDto;
import com.design.service.api.dto.resp.TradeResp;

public class SpotRefundDetailServiceImpl implements IRefundDetailService {
	private static Logger log = LoggerFactory.getLogger(SpotRefundDetailServiceImpl.class);
	
	private Map<String,IThirdTradeService> thridTradeServices;
	
	@Resource
	private RefundDetailMapper refundDetailMapper;
	@Resource
	private RefundServiceImpl refundServiceImpl;
	
	@Override
	public void dealRefund(RefundDto refundDto) {
		log.info("现货退款处理开始...");
		Refund refund = refundDto.getRefund();
		TradeMoneyRemainDto tmrDto = refundServiceImpl.getTradeMoneyReamin(refundDto.getOrder().getOrderNo(), refundDto.getRefundNo());
		
		BigDecimal expectMoney = refundDto.getRefundMoney();
		if(tmrDto.getRemainMoney().compareTo(expectMoney)==-1){
			log.info("退款总额超出付款金额,期望退款总额{},支付剩余总额{}",expectMoney,tmrDto.getRemainMoney());
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		Trade trade = tmrDto.getTrade();
		
		RefundDetail refundDetail = new RefundDetail();
		refundDetail.setRefundNo(refund.getRefundNo());
		refundDetail.setRefundDetailNo(refund.getRefundNo());
		refundDetail.setRefundState(RefundState.REFUNDING.getStateCode());
		refundDetail.setPayTypeId(trade.getPayTypeId());
		refundDetail.setCurrencyId(trade.getCurrencyId());
		refundDetail.setTradeNo(trade.getTradeNo());
		refundDetail.setRefundDetailMoney(refund.getRefundMoney());
		refundDetail.setCreateTime(DateUtil.getCurrentDate());
		refundDetailMapper.insert(refundDetail);
		int guard = -99;
		try{
			log.info("拼接第三方请求");
			BaseRefundDto baseRefundDto = new BaseRefundDto();
			baseRefundDto.setOrderNo(refund.getOrderNo());
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
			refundDto.setGuard(guard);
			refundDetail.setUpdateTime(DateUtil.getCurrentDate());
			refundDetailMapper.update(refundDetail);
		}
	}

	public Map<String, IThirdTradeService> getThridTradeServices() {
		return thridTradeServices;
	}

	public void setThridTradeServices(Map<String, IThirdTradeService> thridTradeServices) {
		this.thridTradeServices = thridTradeServices;
	}
}

package com.design.service.impl.pay.alipay;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.json.JSONWriter;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderType;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.dto.OrderDetailInfo;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.api.dto.resp.TradeResp;
import com.design.service.impl.order.CurrencyServiceImpl;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.padload.MailUtil;
import com.design.service.impl.pay.AbstractPayService;
import com.design.service.impl.pay.CommonNotify;
import com.design.service.impl.template.FreeMarkUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class AliPayServiceImpl extends AbstractPayService {
	private static Logger log = LoggerFactory.getLogger(AliPayServiceImpl.class);
	@Resource
	private AlipayUtil alipayUtil;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private CurrencyServiceImpl currencyServiceImpl;
	@Resource
	private FreeMarkUtil freeMarkUtil;
	@Resource
	private MailUtil mailUtil;
	
	@Value("${alipay.notify}")
	private String payNotifyUrl;
	
	@Override
	public String sign(String userNo, String orderNo,String currencyId) {
		Order t_order = getOrderByOrderNo(orderNo,userNo);
		Map<String, String> params = Maps.newTreeMap();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject("尺寸商品");
		model.setOutTradeNo(orderNo);
		model.setTimeoutExpress("120m");
		model.setTotalAmount(t_order.getOrderMoney().toString());
		model.setProductCode("QUICK_MSECURITY_PAY");
		params.put(AlipayConstants.BIZ_CONTENT_KEY, new JSONWriter().write(model, true));
		params.put(AlipayConstants.NOTIFY_URL,payNotifyUrl);
		try {
			String signResult = alipayUtil.rsaSign(params);
			return signResult;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
	}

	
	@Override
	public CommonNotify parseParamToBean(Map<String, String> params) {
		return AlipayNotify.parse(params);
	}
	
	
	@Override
	public boolean signCheck(Map<String, String> params) {
		try {
			return alipayUtil.rsaCheckV1(params);
		} catch (AlipayApiException e) {
			throw new DesignException(e.getMessage());
		}
	}

	
	@Override
	public void checkNotify(Order order, CommonNotify notifyBean) {
		AlipayNotify alipayNotify = (AlipayNotify)notifyBean;
		
		/**
		 * 验证
		 * 1、out_trade_no为商户创建的订单号
		 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
		 * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方
		 * 4、验证app_id是否为该商户本身
		 */
		if(!new ChicunMoney(alipayNotify.getTotal_amount()).equals(order.getOrderMoney())){
			log.info("订单金额与支付金额不匹配：订单金额#{},支付金额：#{}",order.getOrderMoney(),alipayNotify.getTotal_amount());
			throw new DesignException("订单金额与支付金额不匹配");
		}
		if(!alipayUtil.getApp_id().equals(alipayNotify.getApp_id())){
			log.info("app_id不匹配");
			throw new DesignException("app_id不匹配");			
		}
		if(!alipayUtil.getSeller_id().equals(alipayNotify.getSeller_id())){
			log.info("seller_id不匹配");
			throw new DesignException("seller_id不匹配");
		}
	
	}

	@Override
	public TradeResp tradeRefund(BaseRefundDto refundDto) {
		AlipayTradeRefundResponse refundResp = alipayUtil.tradeRefund(refundDto.getOrderNo(),refundDto.getTradeNo(),refundDto.getRefundNo(),refundDto.getRefundMoney());
		TradeResp result = new TradeResp();
		if(refundResp.isSuccess()){
			log.info("支付宝交易退款成功");
			result.setSuccess(Boolean.TRUE);
		}else{
			log.info("支付宝退款失败,{},{}",refundResp.getSubCode(),refundResp.getSubMsg());
			result.setSubCode(refundResp.getSubCode());
			result.setSubMsg(refundResp.getSubMsg());
			result.setSuccess(Boolean.FALSE);
		}
		return result;
	}



	@Override
	public TradeQueryResp tradeClose(String orderNo, String tradeNo) {
		TradeQueryResp result = new TradeQueryResp();
		
		AlipayTradeQueryResponse queryResp = alipayUtil.tradeQuery(orderNo,tradeNo);
		if(queryResp.isSuccess()){
			//查询成功
			if("WAIT_BUYER_PAY".equals(queryResp.getTradeStatus())){
				log.info("交易创建,等待付款,则调用支付宝关闭接口");
				//交易创建,等待付款,则调用支付宝关闭接口
				AlipayTradeCloseResponse closeResp = alipayUtil.tradeClose(orderNo,tradeNo);
				if(closeResp.isSuccess()){
					log.info("支付宝交易关闭成功,关闭尺寸订单");
					result.setCanClose(true);
				}
			}else{
				log.info("尺寸订单不允许关闭,支付宝交易状态：{}",queryResp.getTradeStatus());
				throw new DesignException(DesignEx.ORDER_CANOTCLOSE);
			}
		}else{
			if("ACQ.TRADE_NOT_EXIST".equals(queryResp.getSubCode())){
				log.info("支付宝交易不存在,执行关闭订单");
				result.setCanClose(true);
			}else{
				log.info("调用支付宝查询接口错误");
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
		}
		return result;
	}


	public void test(String userNo,String orderNo) {
		Order master_order = orderServiceImpl.getOrderByOrderNo(orderNo, userNo);
		Map<String,Object> param = Maps.newHashMap();
		param.put("address", master_order.getAddress());
		param.put("payId", master_order.getPayId());
		param.put("payAbleMoney",master_order.getOrderMoney());
		if(StringUtils.isNotEmpty(master_order.getCurrencyId())){
			String sign = currencyServiceImpl.getCurrency(master_order.getCurrencyId()).getSign();
			param.put("outSign", sign);
		}
		List<String> orderNos = Lists.newArrayList(); 
		if(!OrderType.PRE_SELL.getTypeCode().equals(master_order.getOrderType())){
			//非预售
			orderNos.add(master_order.getOrderNo());
		}
		if(!OrderType.SALE.getTypeCode().equals(master_order.getOrderType())){
			//非现货
			List<Order> splitOrders = orderServiceImpl.getSplitOrders(master_order.getOrderNo());
			for(Order tmp:splitOrders){
				orderNos.add(tmp.getOrderNo());
			}
		}
		int iMax = orderNos.size()-1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(orderNos.get(i));
            if (i == iMax)
                break;
            b.append(", ");
        }
		param.put("orderNos", b.toString());
		
		
		Map<String,String> attachment = Maps.newHashMap();
		String inSign = null;
		JSONArray jsonArray = new JSONArray();
		List<OrderDetailInfo> orderDetailInfos = orderServiceImpl.getOrderDetailInfos(orderNos);
		for(OrderDetailInfo orderDetailInfo:orderDetailInfos){
			orderDetailInfo.setPrice(new ChicunMoney(orderDetailInfo.getPtsumMoney()).divide(orderDetailInfo.getPtBuyNum()).getMoney());
			jsonArray.add(orderDetailInfo);
			if(StringUtils.isNotEmpty(orderDetailInfo.getPicture())){
				attachment.put(orderDetailInfo.getPicture(), orderDetailInfo.getPicturepPath());
			}
			if(inSign==null){
				if(StringUtils.isNotEmpty(orderDetailInfo.getCurrencyId())){
					String sign = currencyServiceImpl.getCurrency(orderDetailInfo.getCurrencyId()).getSign();
					param.put("inSign", sign);
				}
			}
		}
		
		param.put("orderDetails", orderDetailInfos);
		
		JSONObject addressJson = JSONObject.parseObject(master_order.getAddress());
		log.info("参数：{}",param);
		String content = freeMarkUtil.getContent(param, "orderreceipt.ftl");
		log.info("邮件内容为:{}",content);
		
		mailUtil.transport2("CHICUN尺寸—密码找回验证邮件", content, attachment, addressJson.get("email").toString());
	}


}

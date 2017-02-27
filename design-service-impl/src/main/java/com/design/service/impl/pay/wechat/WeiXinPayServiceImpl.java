package com.design.service.impl.pay.wechat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.ChicunMoney;
import com.design.dao.entity.Order;
import com.design.dao.persist.RefundLogMapper;
import com.design.dao.persist.RefundMapper;
import com.design.dao.persist.TradeLogMapper;
import com.design.dao.persist.TradeMapper;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.pay.PayNotifyResp;
import com.design.service.api.dto.pay.wechat.WxCloseReq;
import com.design.service.api.dto.pay.wechat.WxCloseResp;
import com.design.service.api.dto.pay.wechat.WxQueryReq;
import com.design.service.api.dto.pay.wechat.WxQueryResp;
import com.design.service.api.dto.pay.wechat.WxRefundReq;
import com.design.service.api.dto.pay.wechat.WxRefundResp;
import com.design.service.api.dto.pay.wechat.WxUnifiedOrderReq;
import com.design.service.api.dto.pay.wechat.WxUnifiedOrderResp;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.api.dto.resp.TradeResp;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.pay.AbstractPayService;
import com.design.service.impl.pay.CommonNotify;
import com.google.common.collect.Maps;

@Service
public class WeiXinPayServiceImpl extends AbstractPayService {
	
	private static Logger log = LoggerFactory.getLogger(WeiXinPayServiceImpl.class);
	
	@Resource
	private WEChatUtil weChatUtil;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private TradeMapper tradeMapper;
	@Resource
	private TradeLogMapper tradeLogMapper;
	@Resource
	private RefundMapper refundMapper;
	@Resource
	private RefundLogMapper refundLogMapper;
	
	@Override
	public String sign(String userNo, String orderNo,String currencyId) {
		Order t_order = getOrderByOrderNo(orderNo, userNo);
		WxUnifiedOrderReq req = new WxUnifiedOrderReq();
		req.setOut_trade_no(orderNo);
		req.setTotal_fee(new ChicunMoney(t_order.getOrderMoney()).multiply(100).getMoney().intValue());
		req.setBody("尺寸"+t_order.getOrderNo());
		WxUnifiedOrderResp resp = weChatUtil.unifiedorder(req);
		if("SUCCESS".equals(resp.getReturn_code())){
			if("SUCCESS".equals(resp.getResult_code())){
//				JSONObject json = new JSONObject();
//				json.put("prepay_id", resp.getPrepay_id());
//				json.put("sign", resp.getSign());
				return "prepayId="+resp.getPrepay_id();
			}else{
				log.info("微信签名失败,{}",resp);
				throw new DesignException(resp.getErr_code_des());
			}
//			
		}else{
			log.info("微信签名失败,{}",resp);
			throw new DesignException(resp.getReturn_msg());
		}
		
	}
	
	public PayNotifyResp wxNotify(String xml) throws DocumentException {
		PayNotifyResp result = new PayNotifyResp();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> nodes = root.elements();
		Map<String,String> param = Maps.newTreeMap();
		for(Element t:nodes){
			param.put(t.getName(), String.valueOf(t.getData()));
		}
		if(Constant.SUCCESS.equals(param.get("return_code"))){
			PayNotifyResp payNotifyResult = payNotify(param);
			return payNotifyResult;
		}else{
			result.setReturn_code(param.get("return_code"));
			result.setReturn_msg(param.get("return_msg"));
		}
		return result;
	}
	
	@Override
	public CommonNotify parseParamToBean(Map<String, String> params) {
		return WechatNotify.parse(params);
	}
	
	
	@Override
	public boolean signCheck(Map<String, String> params) {
		log.info("微信signCheck,{}",params);
		String sign = weChatUtil.sign(params);
		if(params.get("sign").equals(sign)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void checkNotify(Order order, CommonNotify notifyBean) {
		WechatNotify wechatNotify = (WechatNotify)notifyBean;
		if(!new ChicunMoney(wechatNotify.getTotal_amount()).equals(order.getOrderMoney())){
			log.info("订单金额与支付金额不匹配：订单金额#{},支付金额：#{}",order.getOrderMoney(),wechatNotify.getTotal_amount());
			throw new DesignException("订单金额与支付金额不匹配");
		}
	}
	
	@Override
	public TradeResp tradeRefund(BaseRefundDto refundDto) {
		WxRefundReq req = new WxRefundReq();
		weChatUtil.setCommonReq(req);
		req.setOut_trade_no(refundDto.getOrderNo());
		req.setOut_refund_no(refundDto.getRefundNo());
		req.setTransaction_id(refundDto.getTradeNo());
		req.setTotal_fee(refundDto.getPayMoney().multiply(new BigDecimal(100)).intValue());
		req.setRefund_fee(refundDto.getRefundMoney().multiply(new BigDecimal(100)).intValue());
		req.setOp_user_id(req.getAppid());
		req.setSign(weChatUtil.sign(req));
		WxRefundResp refundResp = weChatUtil.tradeRefund(req);
		TradeResp result = new TradeResp();
		if(Constant.SUCCESS.equals(refundResp.getReturn_code())){
			log.info("微信交易退款请求发送成功");
			if(Constant.SUCCESS.equals(refundResp.getResult_code())){
				log.info("微信交易退款业务成功");
				result.setSuccess(Boolean.TRUE);
			}else{
				log.info("微信交易退款业务失败,{},{}",refundResp.getErr_code(),refundResp.getErr_code_des());
				result.setSubCode(refundResp.getErr_code());
				result.setSubMsg(refundResp.getErr_code_des());
				result.setSuccess(Boolean.FALSE);
			}
		}else{
			log.info("微信退款请求发送失败,{},{}",refundResp.getReturn_code(),refundResp.getReturn_msg());
			result.setSubCode(refundResp.getReturn_code());
			result.setSubMsg(refundResp.getReturn_msg());
			result.setSuccess(Boolean.FALSE);
		}
		return result;
	}


	@Override
	public TradeQueryResp tradeClose(String orderNo, String tradeNo) {
		TradeQueryResp result = new TradeQueryResp();
		WxQueryReq queryReq = new WxQueryReq();
		weChatUtil.setCommonReq(queryReq);
		queryReq.setSign(weChatUtil.sign(queryReq));
		
		WxQueryResp queryResp =  weChatUtil.tradeQuery(queryReq);
		if(Constant.SUCCESS.equals(queryResp.getReturn_code())){
			log.info("微信交易查询请求发送成功");
			if(Constant.SUCCESS.equals(queryResp.getResult_code())){
				log.info("微信交易查询业务成功");
				if("NOTPAY".equals(queryResp.getTrade_state())){
					log.info("交易创建,等待付款,则调用微信关闭接口");
					//交易创建,等待付款,则调用微信关闭接口
						WxCloseReq closeReq = new WxCloseReq();
						weChatUtil.setCommonReq(closeReq);
						closeReq.setSign(weChatUtil.sign(closeReq));
						WxCloseResp closeResp = weChatUtil.tradeClose(closeReq);
						if(Constant.SUCCESS.equals(closeResp.getReturn_code())&&Constant.SUCCESS.equals(closeResp.getResult_code())){
							log.info("微信交易关闭成功,关闭尺寸订单");
							result.setCanClose(true);
						}
				}else{
					log.info("尺寸订单不允许关闭,微信交易状态：{}",queryResp.getTrade_state());
					throw new DesignException(DesignEx.ORDER_CANOTCLOSE);
				}
			}else{
				log.info("微信交易查询业务失败,{},{}",queryResp.getErr_code(),queryResp.getErr_code_des());
				if("ORDERNOTEXIST".equals(queryResp.getErr_code())){
					log.info("微信交易不存在,执行关闭订单");
					result.setCanClose(true);
				}else{
					log.info("调用微信查询接口错误");
					throw new DesignException(DesignEx.INTERNAL_ERROR);
				}
			}
		}else{
			log.info("微信查询请求发送失败,{},{}",queryResp.getReturn_code(),queryResp.getReturn_msg());
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		return result;
	}

	

	

	
}

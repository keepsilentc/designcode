package com.design.service.impl.pay.paypal;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.HttpUtil;
import com.design.common.utils.SignUtil;
import com.design.dao.entity.Order;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.api.dto.resp.TradeResp;
import com.design.service.impl.order.CurrencyServiceImpl;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.pay.AbstractPayService;
import com.design.service.impl.pay.CommonNotify;
import com.google.common.collect.Maps;

@Service
public class PayPalServiceImpl extends AbstractPayService {
	
	private static Logger log = LoggerFactory.getLogger(PayPalServiceImpl.class);
	
	@Resource
	private CurrencyServiceImpl currencyServiceImpl;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	
	@Value("${sign.key}")
	private String signKey;
	@Value("${paypal.ipnverify}")
	private String paypalVerifyUrl;
	
	@Override
	public TradeResp tradeRefund(BaseRefundDto refundDto) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public String sign(String userNo, String orderNo,String currencyId) {
		Order t_order = getOrderByOrderNo(orderNo,userNo);
		BigDecimal totalMoney = currencyServiceImpl.transfer(t_order.getCurrencyId(), t_order.getOrderMoney(), currencyId);
		Map<String,String> params = Maps.newHashMap();
		params.put("currencyId", currencyId);
		params.put("totalMoney", totalMoney.toString());
		params.put("key", signKey);
		params.put("sign", SignUtil.sign(params));
		return HttpUtil.paramToQueryString(params, "UTF-8");
	}

	@Override
	public TradeQueryResp tradeClose(String orderNo, String tradeNo) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public CommonNotify parseParamToBean(Map<String, String> params) {
		return PayPalNotify.parse(params);
	}

	@Override
	public boolean signCheck(Map<String, String> params) {
		params.put("cmd", "_notify-validate");
		String res = HttpUtil.post(paypalVerifyUrl, params);
		log.info("PayPal 对 IPN 回发的回复信息,{}",res);
		
		if("VERIFIED".equals(res)){
			return true;
		}else if (res.equals("INVALID")) {  
	        //非法信息，可以将此记录到您的日志文件中以备调查  
	    } else {  
	        //处理其他错误  
	  
	    }
		return false;
	}
	
	@Override
	public void checkNotify(Order t_order, CommonNotify notifyBean) {
		PayPalNotify paypalNotify = (PayPalNotify)notifyBean;
		if("Completed".equals(paypalNotify.getPaymentStatus())){
			String currencyId = notifyBean.getCurrency_id();
			BigDecimal orderMoney = currencyServiceImpl.transfer(t_order.getCurrencyId(), t_order.getOrderMoney(), currencyId);
			BigDecimal freight = currencyServiceImpl.transfer(t_order.getCurrencyId(), t_order.getFreight(), currencyId);
			BigDecimal discountMoney = currencyServiceImpl.transfer(t_order.getCurrencyId(), t_order.getDiscountMoney(), currencyId);
			t_order.setCurrencyId(currencyId);
			t_order.setOrderMoney(orderMoney);
			t_order.setFreight(freight);
			t_order.setDiscountMoney(discountMoney);
			if(!new ChicunMoney(paypalNotify.getTotal_amount()).equals(orderMoney)){
				log.info("订单金额与支付金额不匹配：订单金额#{},支付金额：#{}",orderMoney,paypalNotify.getTotal_amount());
				throw new DesignException("订单金额与支付金额不匹配");
			}
			orderServiceImpl.updateOrderCurrency(t_order);
		}else{
			log.info("payal交易未完成");
			throw new DesignException(DesignEx.PayPay_NOT_COMPLETED);
		}
	}

}

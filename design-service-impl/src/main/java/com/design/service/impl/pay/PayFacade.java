package com.design.service.impl.pay;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.service.api.IThirdTradeService;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.impl.padload.MailUtil;

public class PayFacade {
	
	private static Logger log = LoggerFactory.getLogger(PayFacade.class);
	
	private Map<String,IThirdTradeService> thirdTradeServices;
	
	@Resource
	private MailUtil mailUtil;
	
	public Map<String, IThirdTradeService> getThirdTradeServices() {
		return thirdTradeServices;
	}

	public void setThirdTradeServices(Map<String, IThirdTradeService> thirdTradeServices) {
		this.thirdTradeServices = thirdTradeServices;
	}


	public String sign(String userNo, String orderNo,String payId,String currencyId) {
		IThirdTradeService thirdTradeServiceImpl = thirdTradeServices.get(payId);
		if(thirdTradeServiceImpl==null){
			log.info("不存在的支付方式");
			throw new DesignException(DesignEx.PAYTYPE_NOT_EXIST);
		}
		return thirdTradeServiceImpl.sign(userNo, orderNo,currencyId);
	}

	public void payConfirm(String userNo, String orderNo) {
		IThirdTradeService thirdTradeServiceImpl = thirdTradeServices.get(Constant.DEFAULT);
		thirdTradeServiceImpl.payConfirm(userNo,orderNo);
//		mailUtil.transport("测试环境用户下单啦", "订单号:"+orderNo,"355200479@qq.com","z.miao.cn@outlook.com","783868970@qq.com","342283662@qq.com");
		mailUtil.transport("测试环境用户下单啦", "订单号:"+orderNo,"783868970@qq.com","342283662@qq.com");
	}

	public TradeQueryResp tradeQuery(String orderNo, String tradeNo,String payId) {
		IThirdTradeService thirdTradeServiceImpl = thirdTradeServices.get(payId);
		if(thirdTradeServiceImpl==null){
			log.info("不存在的支付方式");
			throw new DesignException(DesignEx.PAYTYPE_NOT_EXIST);
		}
		return thirdTradeServiceImpl.tradeClose(orderNo, tradeNo);
	}
	
}

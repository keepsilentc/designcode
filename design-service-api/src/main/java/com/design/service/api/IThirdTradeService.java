package com.design.service.api;

import java.util.Map;

import com.design.dao.entity.Trade;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.pay.PayNotifyResp;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.api.dto.resp.TradeResp;


public interface IThirdTradeService {

	TradeResp tradeRefund(BaseRefundDto refundDto);
	
	String sign(String userNo,String orderNo,String currencyId);

	void payConfirm(String userNo, String orderNo);

	TradeQueryResp tradeClose(String orderNo, String tradeNo);
	
	PayNotifyResp payNotify(Map<String, String> params);

	Trade getTradeByOrderNo(String orderNo);
}

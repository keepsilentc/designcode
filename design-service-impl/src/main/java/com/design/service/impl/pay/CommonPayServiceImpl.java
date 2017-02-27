package com.design.service.impl.pay;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.design.dao.entity.Order;
import com.design.service.api.dto.BaseRefundDto;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.api.dto.resp.TradeResp;

@Service
public class CommonPayServiceImpl extends AbstractPayService{

	@Override
	public TradeResp tradeRefund(BaseRefundDto refundDto) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public String sign(String userNo, String orderNo,String currencyId) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public TradeQueryResp tradeClose(String orderNo, String tradeNo) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public CommonNotify parseParamToBean(Map<String, String> params) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public boolean signCheck(Map<String, String> params) {
		throw new UnsupportedOperationException("不支持的操作");
	}

	@Override
	public void checkNotify(Order order, CommonNotify notifyBean) {
		throw new UnsupportedOperationException("不支持的操作");
	}

}

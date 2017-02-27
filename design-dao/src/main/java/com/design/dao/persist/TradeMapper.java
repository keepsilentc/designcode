package com.design.dao.persist;

import com.design.dao.entity.Trade;


public interface TradeMapper {

	void insert(Trade trade);

	Trade getTradeByOrderNo(String orderNo);

}

package com.design.service.api.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.design.dao.entity.Refund;
import com.design.dao.entity.Trade;

@Getter
@Setter
public class TradeMoneyRemainDto {
	private String orderNo;
	private Trade trade;
	private List<Refund> refundList;
	private BigDecimal remainMoney;
	private Integer guard;
}

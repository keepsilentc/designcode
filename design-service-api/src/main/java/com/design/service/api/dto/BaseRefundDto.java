package com.design.service.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRefundDto {
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 交易号
	 */
	private String tradeNo;
	/**
	 * 退款请求号
	 */
	private String refundNo;
	/**
	 * 退款金额
	 */
	private BigDecimal refundMoney;
	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;;
}

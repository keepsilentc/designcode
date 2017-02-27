package com.design.dao.entity;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RefundDetail extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6659782034497403709L;
	/**
	 * 退款详情号(针对预售商品多次退款请求)
	 */
	private String refundDetailNo;
	/**
	 * 退款请求号
	 */
	private String refundNo;
	/**
	 * 支付交易号
	 */
	private String tradeNo;
	/**
	 * 货币id
	 */
	private String currencyId;
	/**
	 * 支付方式id 
	 */
	private String payTypeId;
	/**
	 * 此次退款金额
	 */
	private BigDecimal refundDetailMoney;
	/**
	 * 退款状态
	 */
	private Integer refundState;
	
}

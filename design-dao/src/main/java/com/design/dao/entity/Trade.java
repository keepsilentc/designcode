package com.design.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

//交易表实体
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Trade extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6846326440462865204L;
	/**
	 * 客户号
	 */
	private String userNo;
	/**
	 * 货币id
	 */
	private String currencyId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单总金额
	 */
	private BigDecimal orderMoney;
	/**
	 * 支付方式id 
	 */
	private String payTypeId;
	/**
	 * 交易流水号
	 */
	private String tradeNo;
	/**
	 * 付款时间
	 */
	private Date payTime;
	/**
	 * 付款金额
	 */
	private BigDecimal payMoney;
	
}

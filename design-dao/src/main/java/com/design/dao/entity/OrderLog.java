package com.design.dao.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class OrderLog extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6027919529419590347L;
	/**
	 * 日志跟踪号
	 */
	private String traceLogId;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单类型
	 */
	private Integer  orderType;
	/**
	 * 货币类型
	 */
	private String currencyId;
	/**
	 * 订单金额
	 */
	private BigDecimal orderMoney;
	/**
	 * 失败原因
	 */
	private String failReason;
	/**
	 * 操作类型
	 */
	private Integer operateType;
}

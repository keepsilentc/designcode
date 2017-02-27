package com.design.dao.entity;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReturnLog extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5005876960552507668L;
	/**
	 * 日志跟踪号
	 */
	private String traceLogId;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 退换货号
	 */
	private String returnNo;
	/**
	 * 订单详细id
	 */
	private Long orderDetailId;
	/**
	 * 退换货个数
	 */
	private Integer returnNum;
	/**
	 * 退换货类型
	 * 1、换货
	 * 2、退货
	 */
	private Integer returnType;
	/**
	 * 记录类型,
	 * 0-申请(来自用户)
	 * 1-审批(来自cms)
	 */
	private Integer recordType;
	
	/**
	 * 原因,与记录类型联合使用
	 */
	private String reason;
	
	/**
	 * 附件,与记录类型联合使用
	 */
	private String pictures;
	/**
	 * 退款状态
	 */
	private Integer returnState;
}

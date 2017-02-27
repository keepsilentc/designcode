package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Returns extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8617139200053203040L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 订单详细id
	 */
	private Long orderDetailId;
	/**
	 * 退换货号
	 */
	private String returnNo;
	/**
	 * 退换货个数
	 */
	private Integer returnNum;
	/**
	 * 退换货申请状态
	 * -1、退换货申请中
	 * 1、退换货批准
	 * 0、退换货未批准
	 * 退换货状态
	 * 11、换货中
	 * 12、换货成功
	 * 14、换货失败
	 * 
	 * 21、退货中
	 * 22、退货成功
	 * 24、退货失败
	 * 
	 */
	private Integer returnState;
	/**
	 * 退换货类型
	 * 1、换货
	 * 2、退货
	 */
	private Integer returnType;
	/**
	 * 物流单号
	 */
	private String deliverNo;
	/**
	 * 物流名称
	 */
	private String deliverName;
	/**
	 * 失败原因
	 */
	private String failReason;
	
}

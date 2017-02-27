package com.design.service.impl.returns;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.ReturnLog;

@Getter
@Setter
@ToString
public class ReturnsDto {
	/**
	 * 退换货类型
	 */
	private Integer returnType;
	/**
	 * 退换货个数
	 */
	private Integer returnNum;
	/**
	 * 退款原因
	 */
	private String reason;
	/**
	 * 附件
	 */
	private String pictures;
	/**
	 * 订单信息
	 */
	private Order order;
	/**
	 * 订单详细信息
	 */
	private OrderDetail orderDetail;
	private ReturnLog returnLog;
	/**
	 * 是否可以关闭
	 */
	private boolean canClose;
	
	private String returnNo;
}

package com.design.dao.entity;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Refund extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6659782034497403709L;
	/**
	 * 退款请求号
	 */
	private String refundNo;
	/**
	 * 客户号
	 */
	private String userNo;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 退款类型
	 * 1-退款
	 * 2-退货
	 */
	private Integer refundType;
	/**
	 * 货币id
	 */
	private String currencyId;
	/**
	 * 订单详细ID
	 */
	private Long orderDetailId;
	/**
	 * 支付方式id 
	 */
	private String payTypeId;
	/**
	 * 退款个数
	 */
	private Integer refundNum;
	/**
	 * 退款金额
	 */
	private BigDecimal refundMoney;
	/**
	 * 退款状态
	 */
	private Integer refundState;
	
}

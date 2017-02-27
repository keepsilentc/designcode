package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RefundLog extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6615207419151394116L;
	/**
	 * 日志跟踪号
	 */
	private String traceLogId;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 退款类型
	 * 1-退款
	 * 2-退货
	 */
	private Integer refundType;
	/**
	 * 订单详细id
	 */
	private Long orderDetailId;
	/**
	 * 退款商品个数
	 */
	private Integer refundNum;
	/**
	 * 退款交易号
	 */
	private String refundNo;
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
	 * 退款支付方式
	 */
	private String payTypeId;
	/**
	 * 退款状态
	 */
	private Integer refundState;
	
	private Integer guard;
	
}

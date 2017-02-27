package com.design.dao.entity;

//支付log表实体
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TradeLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7922415213787200464L;
	/**
	 * 日志跟踪号
	 */
	private String traceLogId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 支付方式id 
	 */
	private String payTypeId;
	/**
	 * 交易流水号
	 */
	private String tradeNo;
	/**
	 * 验签结果
	 */
	private Integer isSignVerified;
	/**
	 * 异步通知处理结果
	 */
	private Integer isSuccess;
	/**
	 * 失败原因
	 */
	private String failReason;
	/**
	 * 通知内容
	 */
	private String notify;
	
}

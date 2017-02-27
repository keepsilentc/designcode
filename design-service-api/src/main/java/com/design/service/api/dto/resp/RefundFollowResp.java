package com.design.service.api.dto.resp;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundFollowResp implements Cloneable{
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 订单详细id
	 */
	private String orderDetailId;
	/**
	 * 退款商品个数
	 */
	private String refundNum;
	/**
	 * 退款描述
	 */
	private String refundDesc;
	/**
	 * 退款交易号
	 */
	private String refundNo;
	/**
	 * 记录类型,
	 * 0-申请(来自用户)
	 * 1-审批(来自cms)
	 */
	private String recordType;
	
	/**
	 * 原因,与记录类型联合使用
	 */
	private String reason;
	
	/**
	 * 退款状态
	 */
	private String refundState;
	/**
	 * 
	 */
	@JSONField(format="yyyy/MM/dd HH:mm")
	private Date refundTime;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}

package com.design.service.api.dto.resp;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderListResp {
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单类型
	 */
	private String orderType;
	/**
	 * 订单金额
	 */
	private String orderMoney;
	/**
	 * 预付金额
	 */
	private String frontMoney;
	/**
	 * 订单状态
	 */
	private String orderState;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
}

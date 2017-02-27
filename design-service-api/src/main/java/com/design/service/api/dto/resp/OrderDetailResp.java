package com.design.service.api.dto.resp;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDetailResp {
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 总金额
	 */
	private String totalMoney;
	/**
	 * 折扣金额
	 */
	private String discountMoney;
	/**
	 * 折扣码
	 */
	private String couponNo;
	/**
	 * 订单类型
	 */
	private String orderType;
	/**
	 * 物流单号
	 */
	private String deliverNo;
	/**
	 * 物流名称
	 */
	private String deliverName;
	/**
	 * 币种
	 */
	private String currencyId;
	/**
	 * 币种符号
	 */
	private String currencySign;
	/**
	 * 付款方式
	 */
	private String payName;
	/**
	 * 订单状态
	 */
	private String orderState;
	/**
	 * 订单金额
	 */
	private String orderMoney;
	/**
	 * 备注
	 */
	private String remark;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 订单详情
	 */
	private List<OrderPtscDetail> ptscDetails;
}

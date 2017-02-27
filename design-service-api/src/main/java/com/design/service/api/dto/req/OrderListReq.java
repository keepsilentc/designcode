package com.design.service.api.dto.req;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderListReq {
	/**
	 * 订单状态转换
	 * 0-全部
	 * 1-待付款
	 * 2-预售
	 * 3-已付款
	 * 4-售后
	 */
	@NotNull
	private Integer orderStateConvert;
	/**
	 * 1、一周月内
	 * 2、一个月内
	 * 3、三个月内
	 */
	private Integer timeRange;
	private Integer pageIndex;
	private Integer pageSize;
}

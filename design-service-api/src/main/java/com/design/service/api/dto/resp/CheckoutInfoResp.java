package com.design.service.api.dto.resp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckoutInfoResp {
	/**
	 * 运费
	 */
	private String freight;
	/**
	 * checkout金额
	 */
	private String checkoutMoney;
	/**
	 * 应付款
	 */
	private String payAbleMoney;
	/**
	 * 折扣金额
	 */
	private String discountMoney;
	/**
	 * 折扣描述
	 */
	private String discountDescribe;
	/**
	 * checkout详情
	 */
	private List<CartProductInfoResp> checkoutInfoDetails;
}

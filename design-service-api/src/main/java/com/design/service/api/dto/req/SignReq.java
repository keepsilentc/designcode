package com.design.service.api.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString
public class SignReq {
	/**
	 * 支付方式id
	 */
	@NotBlank
	private String payId;
	/**
	 * 币种
	 */
	@NotBlank
	private String currencyId;
	/**
	 * 订单编号
	 */
	@NotBlank
	private String orderNo;
	
	
}

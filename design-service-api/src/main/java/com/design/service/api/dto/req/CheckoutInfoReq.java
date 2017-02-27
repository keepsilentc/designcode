package com.design.service.api.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CheckoutInfoReq {
	@NotBlank
	private String cartIds;
	private String couponNo;
	private Long addressId;
	
}

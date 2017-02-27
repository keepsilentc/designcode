package com.design.service.api.dto.req;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateAddressReq {
	@NotNull(message="地址id不能为空")
	private Long addressId;
	@NotBlank(message="手机号不能为空")
	private String mobileNo;
	@NotBlank(message="国家不能为空")
	private String countryId;
	private Long provinceId;
	private Long cityId;
	private Long regionId;
	private String town;
	private String district;
	private String postCode;
	@NotBlank(message="详细地址不能为空")
	private String address;
	private String email;
}

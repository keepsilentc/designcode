package com.design.service.api.dto.req;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddAddressReq {
	@NotBlank(message="姓不能为空")
	private String firstName;
	@NotBlank(message="名不能为空")
	private String lastName;
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

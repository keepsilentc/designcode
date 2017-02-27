package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetAddressResp {
	private String addressId;
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String countryId;
	private String country;
	private String province;
	private String provinceId;
	private String city;
	private String cityId;
	private String region;
	private String regionId;
	private String address;
	private String town;
	private String district;
	private String postCode;
	private String email;
	private String isDefault;
}

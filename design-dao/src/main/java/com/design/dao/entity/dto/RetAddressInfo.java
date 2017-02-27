package com.design.dao.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.RetAddress;

@Getter
@Setter
@ToString
public class RetAddressInfo extends RetAddress{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4684094105072268130L;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String region;
	
}

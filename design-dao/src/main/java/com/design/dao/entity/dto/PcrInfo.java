package com.design.dao.entity.dto;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PcrInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -402892541374449125L;
	private String province;
	private String provinceId;
	private String city;
	private String cityId;
	private String region;
	private String regionId;
}

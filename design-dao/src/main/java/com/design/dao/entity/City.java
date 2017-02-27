package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class City implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7737103600793085017L;
	private Long id;
	private Long provinceId;
	private String cityName;
	
	
}

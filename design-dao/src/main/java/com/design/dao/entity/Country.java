package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Country implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4279918361330240763L;
	/**
	 * 主键id
	 */
	private String id;
	
	private String countryName;
	
	private Integer order;

}

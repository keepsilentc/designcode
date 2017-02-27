package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Region implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9207906441597631024L;
	private Long id;
	private Long cityId;
	private String regionName;
	
}

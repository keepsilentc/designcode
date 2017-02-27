package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SizeType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8487761322755730141L;
	private Long id;
	/**
	 * 尺寸类型名称
	 */
	private String name;
	
}

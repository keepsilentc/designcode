package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Carousel extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8894109474406518289L;
	
	private Integer type;
	private Long picture;
	private Integer orderBy;
	private Integer isEnable;
}

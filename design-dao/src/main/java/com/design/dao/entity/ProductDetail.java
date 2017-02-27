package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5863497794434735573L;
	private String productNo;
	private Long picture;
	private Integer orderBy;
}

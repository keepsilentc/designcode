package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Theme extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001242512680112201L;
	/**
	 * 系列id
	 */
	private Long id;
	/**
	 * 设计师id
	 */
	private Long designerId;
	/**
	 * 系列名称
	 */
	private String themeName;
	/**
	 * 系列描述
	 */
	private String describe;
	/**
	 * 系列图片
	 */
	private Long picture;
	/**
	 * 是否启用
	 */
	private Integer isEnable;
}

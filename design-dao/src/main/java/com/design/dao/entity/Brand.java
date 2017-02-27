package com.design.dao.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Brand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4086471406729711914L;
	private String id;
	/**
	 * 品牌名称
	 */
	private String name;
	/**
	 * 品牌图片
	 */
	private String picture;
	
}

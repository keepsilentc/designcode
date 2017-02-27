package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8060143841957283311L;
	private Long id;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 产品尺寸颜色id
	 */
	private Long ptscId;
	/**
	 * 商品数量
	 */
	private Integer productNum;
	
}

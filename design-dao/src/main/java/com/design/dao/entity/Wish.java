package com.design.dao.entity;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Wish extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2689680989622917934L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 产品尺寸颜色id
	 */
	private Long ptscId;
	/**
	 * 产品编号
	 */
	private String productNo;
}

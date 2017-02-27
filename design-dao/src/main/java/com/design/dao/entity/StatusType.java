package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StatusType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5673803177460377392L;
	/**
	 * 动态类型id
	 */
	private Long id;
	/**
	 * 动态类型名称
	 */
	private String name;
	/**
	 * 是否可购买
	 * 0-否
	 * 1-是
	 */
	private Integer canBuy;
}

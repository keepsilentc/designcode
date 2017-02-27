package com.design.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Currency implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7346626319331306848L;
	/**
	 * 货币id
	 */
	private String id;
	/**
	 * 货币名称
	 */
	private String name;
	/**
	 * 货币符号
	 */
	private String sign;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
}

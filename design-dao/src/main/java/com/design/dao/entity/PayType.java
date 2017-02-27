package com.design.dao.entity;

import java.io.Serializable;
//支付方式表实体
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PayType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4277958189806731956L;
	private String id;
	/**
	 * 支付方式名称
	 */
	private String name;
	/**
	 * 支付方式图标
	 */
	private Long payIcon;
	
}

package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserDesigner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4504084689164992705L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 设计师id
	 */
	private	Long designerId;
	
	/**
	 * 是否关注
	 * 1为是
	 * 0为否
	 */
	private Integer isInterest;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
}

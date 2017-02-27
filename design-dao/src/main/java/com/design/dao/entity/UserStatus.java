package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8384249553331103607L;
	/**
	 * 用户编号
	 */
	private String userNo;
	/**
	 * 动态id
	 */
	private Long statusId;
	/**
	 * 是否点赞
	 * 1为是
	 * 0为否
	 */
	private Integer isPraise;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
}

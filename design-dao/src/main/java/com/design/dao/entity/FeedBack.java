package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FeedBack implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2927387044810961966L;
	/**
	 * 反馈id
	 */
	private Long id;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 反馈名称
	 */
	private String feedBackName;
	/**
	 * 反馈描述
	 */
	private String described;
	/**
	 * 是否阅读
	 * 0-否
	 * 1-是
	 */
	private Integer isRead;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	
}

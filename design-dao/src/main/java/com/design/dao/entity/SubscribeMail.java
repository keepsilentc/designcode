package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscribeMail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6442672780875169402L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 订阅邮件,1-订阅,0-不订阅
	 */
	private Integer isSubscribe;
	/**
	 * 订阅邮件邮箱
	 */
	private String subScribeMail;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
}

package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class CouponUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7445372558039055717L;
	private Long id;
	/**
	 * 客户号
	 */
	private String userNo;
	/**
	 * 优惠券编号
	 */
	private String couponNo;
	/**
	 * 是否可用
	 * 1,可用
	 * 0,不可用
	 */
	private Integer isEnable;
	/**
	 * 用户领取优惠券状态
	 */
	private Integer userCouponState;
	/**
	 * 乐观锁
	 */
	private Integer modifyCount;
	/**
	 * 使用时间
	 */
	private Date useTime;
	/**
	 * 领取时间
	 */
	private Date receiveTime;
}

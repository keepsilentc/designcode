package com.design.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coupon extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6927124501273947683L;
	/**
	 * 优惠券编号
	 */
	private String couponNo;
	/**
	 * 优惠券名称
	 */
	private String couponName;
	/**
	 * 优惠券分类
	 * 0,无限制
	 * 1,单用户使用一次
	 */
	private Integer couponType;
	/**
	 * 优惠券策略,1-打折,2-满减
	 */
	private Integer couponStrategy;
	/**
	 * 总数
	 */
	private Integer sumNum;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 优惠券折扣率
	 */
	private BigDecimal couponRate;
	/**
	 * 满金额
	 */
	private BigDecimal fullMoney;
	/**
	 * 减金额
	 */
	private BigDecimal minusMoney;
	/**
	 * 是否启用
	 */
	private Integer isEnable;
	/**
	 * 备注
	 */
	private String remark;
	
}

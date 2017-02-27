package com.design.dao.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.Coupon;

@Getter
@Setter
@ToString
public class CouponInfo extends Coupon {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8587420330535069346L;

	private Integer remainQuantity;
}

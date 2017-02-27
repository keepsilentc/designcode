package com.design.dao.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.Coupon;
import com.design.dao.entity.dto.CouponInfo;

public interface CouponMapper {
	
	Coupon getCoupon(@Param(value = "couponNo")String couponNo);

	List<Coupon> getCouponListByUserNo(@Param(value = "userNo")String userNo,@Param(value = "validFlag")String validFlag);

	List<CouponInfo> getCouponListByCouponName(String couponName);
	
}

package com.design.dao.persist;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.CouponUser;


public interface CouponUserMapper {

	CouponUser getCouponUser(@Param(value="userNo")String userNo,@Param(value="couponNo")String couponNo);

	int update(CouponUser t_couponUser);

	void insert(CouponUser couponUser);

	int getCouponReceivedQuantity(String couponNo);

	int getCouponUserQuantity(@Param(value="userNo")String userNo,@Param(value="couponNo")String couponNo);

}

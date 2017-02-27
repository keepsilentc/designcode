package com.design.service.api;

import java.math.BigDecimal;
import java.util.List;

import com.design.service.api.dto.resp.CouponResp;


public interface ICouponService {
	
	BigDecimal disountMoney(String userNo,String couponNo,BigDecimal amount);
	
	BigDecimal getDisountMoney(String userNo,String couponNo,BigDecimal amount);
	
	List<CouponResp> getUserCouponList(String token, String validFlag);
	
	List<CouponResp> searchCoupon(String couponName);
	
	void lockCoupon(String userNo,String couponNo);

	void receiveCoupon(String token, String couponNo);

	
	
}

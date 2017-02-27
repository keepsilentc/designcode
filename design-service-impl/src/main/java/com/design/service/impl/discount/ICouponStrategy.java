package com.design.service.impl.discount;

import java.math.BigDecimal;

import com.design.dao.entity.Coupon;

public interface ICouponStrategy {
	BigDecimal doStrategy(Coupon t_coupon, BigDecimal amount);
}

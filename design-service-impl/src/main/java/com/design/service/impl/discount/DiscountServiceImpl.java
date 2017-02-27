package com.design.service.impl.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.design.dao.entity.Coupon;

@Service
public class DiscountServiceImpl implements ICouponStrategy{

	@Override
	public BigDecimal doStrategy(Coupon t_coupon, BigDecimal amount) {
		BigDecimal rate = t_coupon.getCouponRate();
		return amount.multiply(new BigDecimal(10).subtract(rate).divide(new BigDecimal(10),2, RoundingMode.DOWN));
	}

}

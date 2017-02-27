package com.design.service.impl.discount;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.design.dao.entity.Coupon;
@Service
public class FullMoneyMinusServiceImpl implements ICouponStrategy{

	@Override
	public BigDecimal doStrategy(Coupon t_coupon, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.design.common.utils;

import java.math.BigDecimal;

public class MoneyUtil {
	
	public static ChicunMoney getHalfMoney(BigDecimal orderMoney){
		return new ChicunMoney(orderMoney).divide(2);
		
	}

	public static ChicunMoney getTotalMoney(BigDecimal frontMoney) {
		return new ChicunMoney(frontMoney).multiply(2);
	}
}

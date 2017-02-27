package com.design.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChicunMoney{
	
	private BigDecimal money;
	
	public ChicunMoney(){
		money = new BigDecimal(0).setScale(2);
	}
	
	public ChicunMoney(Object money){
		this.money = parseBigDecimal(money);
	}
	
	public ChicunMoney multiply(Object money){
		BigDecimal bm = parseBigDecimal(money);
		
		return new ChicunMoney(this.money.multiply(bm).setScale(2, RoundingMode.UP));
	}
	
	public ChicunMoney divide(Object money){
		BigDecimal bm = parseBigDecimal(money);
		
		return new ChicunMoney(this.money.divide(bm,2, RoundingMode.UP));
	}
	
	public ChicunMoney add(Object money){
		BigDecimal bm = parseBigDecimal(money);
		
		return new ChicunMoney(this.money.add(bm).setScale(2, RoundingMode.UP));
	}
	
	public ChicunMoney subtract(Object money){
		BigDecimal bm = parseBigDecimal(money);
		
		return new ChicunMoney(this.money.subtract(bm).setScale(2, RoundingMode.UP));
	}
	
	public BigDecimal setScale(int scale){
		return this.money.setScale(scale, RoundingMode.UP);
	}
	
	public BigDecimal getMoney() {
		return money;
	}

	@Override
	public boolean equals(Object obj) {
		BigDecimal bm = parseBigDecimal(obj);
		return money.equals(bm);
	}

	@Override
	public String toString() {
		return money.toString();
	}
	
	public static BigDecimal parseBigDecimal(Object money) {
		BigDecimal bm = null;
		if(money instanceof Integer){
			bm = new BigDecimal((Integer)money);
		}else if(money instanceof String){
			bm = new BigDecimal((String)money);
		}else if(money instanceof Long){
			bm = new BigDecimal((Long)money);
		}else if(money instanceof Double){
			bm = new BigDecimal((Double)money);
		}else if(money instanceof BigDecimal){
			bm = (BigDecimal)money;
		}else if(money instanceof ChicunMoney){
			bm = ((ChicunMoney)money).getMoney();
		}
		bm = bm.setScale(2, RoundingMode.UP);
		return bm;
	}
	
	public static boolean isInteger(Object money){
		BigDecimal bm = parseBigDecimal(money);
		BigDecimal bm_int = bm.setScale(0, RoundingMode.UP);
		return bm.intValue()==bm_int.intValue();
	}
}

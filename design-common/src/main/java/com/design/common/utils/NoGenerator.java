package com.design.common.utils;

import java.util.Random;
import java.util.UUID;

public class NoGenerator {
	/**
	 * 生成随机4位字母
	 * @return
	 */
	public static String random4Char(){
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<4;i++){
			int temp = random.nextInt(2)<<1==0?65:97;
			builder.append((char)(random.nextInt(26)+temp));
		}
		return builder.toString();
	}
	/**
	 * 生成客户号
	 * @return
	 */
	public static String generateUserNo(){
		StringBuilder builder = new StringBuilder();
		builder.append(random4Char());
		builder.append(DateUtil.getCurrent());
		builder.append(new Random(UUID.randomUUID().hashCode()).nextInt(899)+100);
		return builder.toString();
	}
	
	/**
	 * 生成订单号
	 * @return
	 */
	public static String generateOrderNo(){
		StringBuilder builder = new StringBuilder();
		builder.append(DateUtil.getCurrent());
		builder.append(new Random(UUID.randomUUID().hashCode()).nextInt(8999)+1000);
		return builder.toString();
	}
	
	
	public static String generateCouponNo(String type){
		StringBuilder builder = new StringBuilder();
		if("2".equals(type)){
			builder.append("S");
		}else{
			builder.append("D");
		}
		builder.append(DateUtil.getCurrent());
		builder.append(new Random(UUID.randomUUID().hashCode()).nextInt(899)+100);
		return builder.toString();
	}
	
	public static String generateRefundNo() {
		StringBuilder builder = new StringBuilder();
		builder.append(DateUtil.getCurrent());
		builder.append(new Random(UUID.randomUUID().hashCode()).nextInt(8999)+1000);
		return builder.toString();
	}
}

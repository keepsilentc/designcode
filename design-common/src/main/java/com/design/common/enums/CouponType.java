package com.design.common.enums;

public enum CouponType {
	UNLIMIT(0,"无限制"),
	SINGLEUSER(1,"单用户使用一次"),
	USERLIMIT(2,"唯一用户使用");
	
	private Integer typeCode;
	private String typeDesc;
	
	private CouponType(Integer typeCode, String typeDesc) {
		this.typeCode = typeCode;
		this.typeDesc = typeDesc;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	
}

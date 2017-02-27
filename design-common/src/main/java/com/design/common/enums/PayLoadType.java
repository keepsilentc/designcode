package com.design.common.enums;

public enum PayLoadType {
	DEFAULT(0,"普通推送"),
	NEWGOODS(1,"新品上架"),
	GOODSARRIVAL(2,"预售商品到货"),
	WISHONSALE(3,"愿望清单打折"),
	STATUS(4,"动态推送");
	
	private PayLoadType(Integer typeCode, String typeDesc) {
		this.typeCode = typeCode;
		this.typeDesc = typeDesc;
	}
	private Integer typeCode;
	private String typeDesc;
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
}

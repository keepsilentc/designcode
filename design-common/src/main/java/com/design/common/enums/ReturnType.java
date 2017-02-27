package com.design.common.enums;

public enum ReturnType {
	EXCHANGE(1,"换货"),
	RETURN(2,"退货");
	private Integer typeCode;
	private String typeDes;
	private ReturnType(Integer typeCode, String typeDes) {
		this.typeCode = typeCode;
		this.typeDes = typeDes;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public String getTypeDes() {
		return typeDes;
	}
}

package com.design.common.enums;

public enum RecordType {
	APPLY(1,"申请"),
	APPROVE(2,"审核"),
	DO(3,"执行");
	
	private Integer typeCode;
	private String typeDesc;
	
	private RecordType(Integer typeCode, String typeDesc) {
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

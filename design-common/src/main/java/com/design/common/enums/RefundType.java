package com.design.common.enums;

public enum RefundType {
	REFUND(1,"退款"),
	RETURN(2,"退货");
	private Integer typeCode;
	private String typeDes;
	private RefundType(Integer typeCode, String typeDes) {
		this.typeCode = typeCode;
		this.typeDes = typeDes;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public String getTypeDes() {
		return typeDes;
	}
	public static RefundType get(Integer refundType) {
		for(RefundType tmp :values()){
			if(tmp.typeCode.equals(refundType)){
				return tmp;
			}
		}
		return null;
	}
}

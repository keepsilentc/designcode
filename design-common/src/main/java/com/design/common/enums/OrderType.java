package com.design.common.enums;

public enum OrderType {
	SALE(10,"发售"),
	PRE_SELL(20,"预售"),
	MIX_SELL(30,"混合");
	private Integer typeCode;
	private String typeDes;
	private OrderType(Integer typeCode, String typeDes) {
		this.typeCode = typeCode;
		this.typeDes = typeDes;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public String getTypeDes() {
		return typeDes;
	}
	public static String getOrderTypeDesc(Integer orderType) {
		for(OrderType tmp:OrderType.values()){
			if(tmp.getTypeCode().equals(orderType)){
				return tmp.getTypeDes();
			}
		}
		return "未知";
	}
}

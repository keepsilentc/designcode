package com.design.common.enums;

public enum OrderOperateType {
	CREATE_INIT(10,"创建初始化"),
	CREATE_SUCCESS(20,"创建成功"),
	CREATE_FAIL(40,"创建失败"),
	CLOSE_INIT(11,"关闭初始化"),
	CLOSE_SUCCESS(21,"关闭成功"),
	CLOSE_FAIL(41,"关闭失败");
	private Integer typeCode;
	private String typeDes;
	private OrderOperateType(Integer typeCode, String typeDes) {
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

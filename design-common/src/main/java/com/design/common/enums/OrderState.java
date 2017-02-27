package com.design.common.enums;

public enum OrderState {
	OBLIGATION(10,"待付款"),
	CONFIRM(13,"确认中"),
	PAY_SUCCESS(21,"已付款"),
	PRE_PAY_SUCCESS(22,"已付定金"),
	DELIVERED(30,"已发货"),
	REVEIVED(32,"已签收"),
	SUCCESS(20,"已完成"),
	FAIL(40,"已关闭"),
	;
	private Integer stateCode;
	private String stateDesc;
	private OrderState(Integer stateCode, String stateDesc) {
		this.stateCode = stateCode;
		this.stateDesc = stateDesc;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public String getStateDesc() {
		return stateDesc;
	}
}

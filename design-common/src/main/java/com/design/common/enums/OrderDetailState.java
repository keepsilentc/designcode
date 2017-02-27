package com.design.common.enums;

public enum OrderDetailState {
	
	INIT(-1,"初始状态"),
	
	REFUNDING(10,"退款中"),
	REFUNDSUCCESS(20,"退款成功"),
	REFUNDFAIL(40,"退款失败"),
	CLOSE(50,"关闭"),
	
	EXCHANGEING(11,"换货中"),
	EXCHANGESUCCESS(21,"换货成功"),
	EXCHANGEFAIL(41,"换货失败"),
	
	RETURNING(12,"退货中"),
	RETURNSUCCESS(22,"退货成功"),
	RETURNFAIL(42,"退货失败"),
	;
	private Integer stateCode;
	private String stateDesc;
	private OrderDetailState(Integer stateCode, String stateDesc) {
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

package com.design.common.enums;

public enum ReturnState {
	RETURN_APPLYING(-1,"退换货申请中"),
	RETURN_APPROVE_PASSS(1,"审批通过"),
	RETURN_APPROVE_NOTPASS(0,"审批不通过"),
	
	EXCHANGEING(11,"换货中"),
	EXCHANGE_SUCCESS(12,"换货成功"),
	EXCHANGE_FAIL(14,"换货失败"),
	
	RETURNING(21,"退货中"),
	RETURN_SUCCESS(22,"退货成功"),
	RETURN_FAIL(24,"退货失败"),
	
	;
	private Integer stateCode;
	private String stateDesc;
	
	private ReturnState(Integer stateCode, String stateDesc) {
		this.stateCode = stateCode;
		this.stateDesc = stateDesc;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateDesc() {
		return stateDesc;
	}
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	
}

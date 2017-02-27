package com.design.common.enums;

import com.design.common.assist.DesignException;

public enum RefundState {
	REFUND_APPROVEING(-1,"提交申请退款"),
	REFUND_APPROVE_FAIL(0,"退款审核失败"),
	REFUND_APPROVE_PASS(1,"退款审核通过"),
	
	REFUNDING(10,"退款执行中"),
	REFUND_SUCCESS(20,"退款成功"),
	REFUND_FAIL(40,"退款失败");
	private Integer stateCode;
	private String stateDesc;
	private RefundState(Integer stateCode, String stateDesc) {
		this.stateCode = stateCode;
		this.stateDesc = stateDesc;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public String getStateDesc() {
		return stateDesc;
	}
	public static RefundState get(Integer refundState) {
		for(RefundState tmp:values()){
			if(tmp.stateCode.equals(refundState)){
				return tmp;
			}
		}
		throw new DesignException(DesignEx.INTERNAL_ERROR);
	}
}

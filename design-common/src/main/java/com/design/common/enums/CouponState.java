package com.design.common.enums;

public enum CouponState {
	/**
	 * 未使用
	 */
	UNUSE(10),
	/**
	 * 已使用
	 */
	USED(30);
	/**
	 * 失效
	 */
//	INVALID(40);
	
	private Integer stateCode;

	private CouponState(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getStateCode() {
		return stateCode;
	}

	
}

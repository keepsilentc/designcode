package com.design.common.enums;

import com.design.common.assist.DesignException;

public enum ProductState{
	SPOT(10,"发售"),
	PRE_SELL(20,"预售");
	private Integer stateCode;
	private String stateDesc;
	private ProductState(Integer stateCode, String stateDesc) {
		this.stateCode = stateCode;
		this.stateDesc = stateDesc;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public String getStateDesc() {
		return stateDesc;
	}
	public static ProductState getByState(Integer productState) {
		for(ProductState tmp:values()){
			if(tmp.stateCode.equals(productState)){
				return tmp;
			}
		}
		throw new DesignException(DesignEx.INTERNAL_ERROR);
	}
}

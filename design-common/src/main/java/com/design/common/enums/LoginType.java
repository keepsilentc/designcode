package com.design.common.enums;

public enum LoginType {
	EMAIL("email"),
	MOBILENO("mobileNo"),
	PLATEFORM("platform");
	private String loginType;
	private LoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getLoginType() {
		return loginType;
	}
	
}

package com.design.common.assist;

import com.design.common.enums.DesignEx;
//尺寸业务逻辑异常(回滚异常)
public class DesignException extends RuntimeException {
	private static final long serialVersionUID = 4166195543562737994L;
	private String errCode;
	
	public DesignException(String errMsg){
		super(errMsg);
	}
	public DesignException(DesignEx ex){
		this(ex.getErrMsg());
		this.errCode = ex.getErrCode();
	}
	public DesignException(String errCode,String errMsg){
		this(errMsg);
		this.errCode = errCode;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}

package com.design.common.assist;

import com.design.common.enums.DesignEx;
//尺寸业务逻辑异常(不回滚异常)
public class DesignSubmitException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1130939060488579102L;
	private String errCode;
	
	public DesignSubmitException(String errMsg){
		super(errMsg);
	}
	public DesignSubmitException(DesignEx ex){
		this(ex.getErrMsg());
		this.errCode = ex.getErrCode();
	}
	public DesignSubmitException(String errCode,String errMsg){
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

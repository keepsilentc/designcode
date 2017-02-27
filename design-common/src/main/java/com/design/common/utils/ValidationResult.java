package com.design.common.utils;

import java.util.Map;
/**
 * 校验结果类
 * @author tc
 *
 */
public class ValidationResult {
	/**
	 * 校验结果是否有错
	 */
	private boolean hasErrors;
	
	private Map<String,String> errorMsg;

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public Map<String, String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(Map<String, String> errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}

package com.design.web.dto;

import com.design.common.assist.Constant;

public class Response<T>{
	/**
	 * 是否成功，true为成功，false为失败
	 */
	private String success = "0";
	private String respCode;
	private String respMessage;
	private T result;
	
	public Response(){
		
	}
	public Response(T result){
		this.success = "1";
		this.result = result;
	}
	
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.respCode = Constant.SUCCESSCODE;
		this.success = "1";
		this.result = result;
	}
	public String getSuccess() {
		return success;
	}
}

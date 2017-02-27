package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubScribeMailResp {
	/**
	 * 订阅邮件,1-订阅,0-不订阅
	 */
	private Integer isSubscribe;
	/**
	 * 订阅邮件邮箱
	 */
	private String subScribeMail;
	
}

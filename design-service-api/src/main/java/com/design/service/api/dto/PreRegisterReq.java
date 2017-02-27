package com.design.service.api.dto;

import org.hibernate.validator.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class PreRegisterReq {
	@NotBlank(message="注册类型为空")
	private String registerType;
	
	@NotBlank(message="电子邮件地址为空",groups=Emails.class)
	private String email;
	
	@NotBlank(message="手机号为空",groups=Mobiles.class)
	private String mobileNo;
	
}

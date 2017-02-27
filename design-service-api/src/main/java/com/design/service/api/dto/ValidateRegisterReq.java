package com.design.service.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidateRegisterReq extends PreRegisterReq{
	@NotBlank(message="验证码为空",groups=Mobiles.class)
	private String registerCode;
}

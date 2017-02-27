package com.design.service.api.dto;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UpdateUserInfoReq {
	private String nickName;
	private String realName;
	@Min(value=1,message="呵呵")
	@Max(value=2,message="呵呵")
	private Integer gender;
	private String birthDay;
}

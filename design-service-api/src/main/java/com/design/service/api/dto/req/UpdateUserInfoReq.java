package com.design.service.api.dto.req;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UpdateUserInfoReq {
	private String nickName;
	private String realName;
	private String gender;
	private String birthday;
	private String email;
	private String mobileNo;
	
}

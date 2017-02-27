package com.design.service.api.dto;

import javax.validation.groups.Default;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class LoginReq {
	
	@NotBlank(message="登陆类型为空",groups=Default.class)
	private String loginType;
	
	@NotBlank(message="手机号为空",groups=Mobiles.class)
	private String mobileNo;
	
	@Email(message="邮箱格式不正确",groups=Emails.class)
	@NotBlank(message="邮箱为空",groups=Emails.class)
	private String email;
	
	@NotBlank(message="密码为空",groups={Mobiles.class,Emails.class})
	private String passwd;
	
	@NotBlank(message="用户在第三方的唯一标识为空",groups=PlatForm.class)
	private String userId;
	/**
	 * 第三方昵称
	 */
	private String nickName;
	/**
	 * WB :微博
	 * WX:微信
	 * GG:google 
	 * FB:facebook
	 */
	@NotBlank(message="第三方登陆类型为空",groups=PlatForm.class)
	private String platform;
	
}

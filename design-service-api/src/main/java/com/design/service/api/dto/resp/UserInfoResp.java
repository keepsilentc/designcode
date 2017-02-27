package com.design.service.api.dto.resp;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoResp {
	/**
	 * 等级id
	 */
	private String levelId;
	/**
	 * 客户号
	 */
	private String userNo;
	/**
	 * 等级名称
	 */
	private String levelName;
	/**
	 * 等级图标
	 */
	private String levelIcon;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 性别 1-男 2-女
	 */
	private Integer gender;
	/**
	 * 年龄
	 */
//	private Integer age;
	/**
	 * 手机号
	 */
	private String mobileNo;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 生日
	 */
	@JSONField(format="yyyy-MM-dd")
	private Date birthDay;
	/**
	 * 照片
	 */
	private String photo;
	/**
	 * 登陆时间
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date loginTime;
	/**
	 * 订阅邮件,1-订阅,0-不订阅
	 */
	private String subScribeMail;
	
}

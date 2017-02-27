package com.design.dao.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1009340038398530712L;
	/**
	 * 等级id
	 */
	private String levelId;
	/**
	 * 第三方平台id
	 */
	private String platformId;
	/**
	 * 第三方平台类型
	 */
	private String platformType;
	/**
	 * 用户账号
	 */
	private String userName;
	/**
	 * 用户密码
	 */
	private String passwd;
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
	private int gender;
	/**
	 * 年龄
	 */
	private int age;
	/**
	 * 手机号
	 */
	private String mobileNo;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 生日
	 */
	private Date birthDay;
	/**
	 * 照片
	 */
	private Long photo;
	/**
	 * 密码连续出错次数
	 */
	private int mistakeNum;
	/**
	 * 设备token
	 */
	private String deviceToken;
	/**
	 * 登陆时间
	 */
	private Date loginTime;
	/**
	 * 是否启用 0-否 1-是
	 */
	private Integer isEnable;
	/**
	 * 备注
	 */
	private String remark;
	
	
}

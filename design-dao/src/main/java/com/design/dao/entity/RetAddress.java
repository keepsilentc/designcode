package com.design.dao.entity;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RetAddress extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544439608239307761L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 姓
	 */
	private String firstName;
	/**
	 * 名
	 */
	private String lastName;
	/**
	 * 联系方式
	 */
	private String mobileNo;
	/**
	 * 国家id
	 */
	private String countryId;
	
	/**
	 * 省份id
	 */
	private Long provinceId;
	
	/**
	 * 城市id
	 */
	private Long cityId;
	
	/**
	 * 地区id
	 */
	private Long regionId;
	/**
	 * 城市
	 */
	private String town;
	/**
	 * 区
	 */
	private String district;
	
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 邮件
	 */
	private String email;
	
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 是否为默认地址
	 * 0-否
	 * 1-是
	 */
	private Integer isDefault;
}

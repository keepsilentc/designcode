package com.design.dao.entity;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Designer extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3577034616425476548L;
	/**
	 * 设计师id
	 */
	private String countryId;
	/**
	 * 设计师名称
	 */
	private String designerName;
	/**
	 * 品牌id
	 */
	private String brandId;
	/**
	 * 照片
	 */
	private Long photo;
	/**
	 * 设计师描述
	 */
	private String describe;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 启用状态
	 */
	private Integer isEnable;
}

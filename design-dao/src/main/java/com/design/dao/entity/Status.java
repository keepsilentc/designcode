package com.design.dao.entity;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Status extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5078964758633342609L;
	/**
	 * 动态类型id
	 */
	private Long statusTypeId;
	/**
	 * 系列id
	 */
	private String themeId;
	/**
	 * 动态详情
	 */
	private String statusDetail;
	/**
	 * 动态详情url
	 */
	private String statusDetailUrl;
	/**
	 * 动态名称
	 */
	private String statusName;
	/**
	 * 动态描述
	 */
	private String describe;
	/**
	 * 图片
	 */
	private Long picture;
	/**
	 * 内部图片
	 */
	private Long insidePicture;
	/**
	 * 排序
	 */
	private Integer orderBy;
	/**
	 * 启用状态
	 */
	private Integer isEnable;
	/**
	 * 图片宽度
	 */
	private Integer pictureWidth;
	/**
	 * 图片高度
	 */
	private Integer pictureHeight;
}

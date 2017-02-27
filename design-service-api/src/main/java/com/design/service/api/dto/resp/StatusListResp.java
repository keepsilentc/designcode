package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusListResp {
	/**
	 * 动态id
	 */
	private String statusId;
	/**
	 * 动态名称
	 */
	private String statusName;
	/**
	 * 外部图片
	 */
	private String picture;
	/**
	 * 国家id
	 */
	private String countryId;
	/**
	 * 点赞数
	 */
	private String markCount;
	/**
	 * 是否关注
	 */
	private String isMarked;
	/**
	 * 是否可以购买
	 */
	private String canBuy;
	/**
	 * 系列id
	 */
	private String themeId;
	/**
	 * 设计师id
	 */
	private String designerId;
	/**
	 * 设计师名称
	 */
	private String designerName;
	/**
	 * 设计师图片
	 */
	private String designerAvatar;
	/**
	 * 动态描述
	 */
	private String describe;
	/**
	 * 外部图片宽度
	 */
	private String pictureWidth;
	/**
	 * 外部图片高度
	 */
	private String pictureHeight;
}

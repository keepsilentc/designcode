package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusDetailResp {
	/**
	 * 动态id
	 */
	private String statusId;
	/**
	 * 系列id
	 */
	private String themeId;
	private String isMarked;
	private String insidePicture;
	private String statusName;
	private String themeName;
	private String canBuy;
	private String designerAvatar;
	private String designerName;
	private String designerId;
	private String countryId;
	private String designerDescribe;
	private String latestGUName;
	private String latestGUAvatar;
	private String goodsCount;
	private String htmlStr;
}

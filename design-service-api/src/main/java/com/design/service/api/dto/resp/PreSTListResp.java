package com.design.service.api.dto.resp;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreSTListResp {
	/**
	 * 系列图片
	 */
	private String picture;
	/**
	 * 系列id
	 */
	private String themeId;
	/**
	 * 系列名称
	 */
	private String themeName;
	/**
	 * 设计师名称
	 */
	private String designerName;
	/**
	 * 设计师头像
	 */
	private String designerAvatar;
	/**
	 * 设计师id
	 */
	private String designerId;
	/**
	 * 预售结束时间
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date preSellEndTime;
	
}

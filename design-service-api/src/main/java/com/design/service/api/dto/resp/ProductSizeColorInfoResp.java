package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductSizeColorInfoResp {
	/**
	 * 尺码名称
	 */
	private String sizeName;
	/**
	 * 尺码id
	 */
	private String sizeId;
	/**
	 * 颜色id
	 */
	private String colorId;
	/**
	 * 颜色图片
	 */
	private String colorPicture;
	/**
	 * 库存id
	 */
	private String ptscId;
	/**
	 * 商品图片
	 */
	private String picture;
	/**
	 * 库存
	 */
	private String inventory;
	/**
	 * 销量
	 */
	private String sale;
	/**
	 * 启用标记
	 */
	private String isEnable;
}

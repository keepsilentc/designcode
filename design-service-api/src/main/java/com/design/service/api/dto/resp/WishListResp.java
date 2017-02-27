package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WishListResp {
	
	/**
	 * 商品图片
	 */
	private String picture;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 国家id
	 */
	private String countryId;
	/**
	 * 库存
	 */
	private String inventory;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 商品尺寸颜色id
	 */
	private String ptscId;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 尺码名称
	 */
	private String sizeName;
	/**
	 * 愿望清单id
	 */
	private String wishId;
	/**
	 * 启用标记
	 */
	private String isEnable;
}

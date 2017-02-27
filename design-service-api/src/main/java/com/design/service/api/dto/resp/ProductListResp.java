package com.design.service.api.dto.resp;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.alibaba.fastjson.annotation.JSONField;

@Getter
@Setter
@ToString
public class ProductListResp {
	/**
	 * 设计师id
	 */
	private String designerId;
	/**
	 * 设计师名称
	 */
	private String designerName;
	/**
	 * 设计师国家id
	 */
	private String countryId;
	/**
	 * 商品号
	 */
	private String productNo;
	/**
	 * 原价
	 */
	private String originPrice;
	/**
	 * 现价
	 */
	private String price;
	/**
	 * 商品图片
	 */
	private String picture;
	/**
	 * 商品状态
	 * 10、现货
	 * 20、预售
	 */
	private String state;
	/**
	 * 是否是新品
	 */
	private String isNew;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 库存
	 */
	private String inventory;
	/**
	 * 预售开始时间
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date preSellStartTime;
	/**
	 * 预售结束时间
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date preSellEndTime;
}

package com.design.service.api.dto.resp;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.alibaba.fastjson.annotation.JSONField;

@Getter
@Setter
@ToString
public class ProductInfoResp {
	/**
	 * 编辑笔记
	 */
	private String productDescribe;
	/**
	 * 单品细节
	 */
	private String productDetail;
	/**
	 * 尺码指导
	 */
	private String sizeDescribe;
	/**
	 * 商品编号
	 */
	private String productNo;
	/**
	 * 商品图片
	 */
	private String picture;

	/**
	 * 原价
	 */
	private String originPrice;
	
	/**
	 * 预售价
	 */
	private String preSalePrice;
	
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 产品名称
	 */
	private String productName;
	
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
	
	/**
	 * 商品公共图
	 */
	private List<String> pictures;
	
	/**
	 * 产品尺寸颜色
	 */
	private List<ProductSizeColorInfoResp> specifications;
}

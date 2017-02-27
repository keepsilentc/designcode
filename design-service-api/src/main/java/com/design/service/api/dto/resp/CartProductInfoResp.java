package com.design.service.api.dto.resp;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class CartProductInfoResp {
	//购物车记录id
	private String cartId;
	//尺寸颜色图片id
	private String ptscId;
	//产品编号
	private String productNo;
	//产品名称
	private String productName;
	//商品图片
	private String picture;
	//商品价格
	private String price;
	//商品状态 10、现货  20、预售
	private String productState;
	//预售开始时间
	private Date preSellStartTime;
	//预售结束时间
	private Date preSellEndTime;
	//商品预售价
	private String preSalePrice;
	//商品数量
	private String productNum;
	/**
	 * 启用标记
	 */
	private String isEnable;
	/**
	 * 产品尺寸颜色
	 */
	private List<ProductSizeColorInfoResp> specifications;
	
}

package com.design.dao.entity.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.design.dao.entity.Cart;
@Getter
@Setter
public class CartInfo extends Cart{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5035612009034164642L;
	
	//产品名称
	private String productName;
	//商品图片
	private Long picture;
	//商品预售价
	private BigDecimal preSalePrice;
	//商品价格
	private BigDecimal price;
	//商品状态 10、现货  20、预售
	private Integer productState;
	//预售开始时间
	private Date preSellStartTime;
	//预售结束时间
	private Date preSellEndTime;
	//启用标记
	private Integer isEnable;
}

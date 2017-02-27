package com.design.dao.entity.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.design.dao.entity.Product;
@Getter
@Setter
public class ProductInfo extends Product{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6611800137120360714L;
	
	private String themeName;
	
	private String countryName;
	
	private String categoryName;
	
	private String countryId;
	
	private String brandName;
	
	private String designerName;
	
	private Integer inventory;
	/**
	 * 预售价
	 */
	private BigDecimal preSalePrice;
}

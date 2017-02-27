package com.design.dao.entity.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.OrderDetail;

@Getter
@Setter
@ToString
public class OrderDetailInfo extends OrderDetail{/**
	 * 
	 */
	private static final long serialVersionUID = 2119648235238120529L;
	private String designerName;
	private String productName;
	private String colorPicture;
	private String colorName;
	private String sizeName;
	private String picture;
	private String picturepPath;
	private BigDecimal price;
}

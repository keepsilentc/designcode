package com.design.dao.entity.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.Wish;
@Getter
@Setter
@ToString
public class WishInfo extends Wish {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1899271586296640368L;
	
	private Long picture;
	private String productName;
	private String countryId;
	private Integer inventory;
	private BigDecimal price;
	private Integer isEnable;
	private String sizeName;
}

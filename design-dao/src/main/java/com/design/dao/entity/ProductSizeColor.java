package com.design.dao.entity;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductSizeColor extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4052010809844508579L;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 尺寸id
	 */
	private Long sizeId;
	/**
	 * 颜色id
	 */
	private Long colorId;

	/**
	 * 此产品对应尺寸和颜色的图片
	 */
	private Long picture;
	/**
	 * 库存数量
	 */
	private Integer inventory;
	/**
	 * 销量
	 */
	private Integer sale;
	/**
	 * 是否启用
	 * 0-否
	 * 1-是
	 */
	private Integer isEnable;
	
	
	
	
}

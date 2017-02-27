package com.design.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4799196786363163583L;
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 系列id
	 */
	private Long themeId;
	/**
	 * 类别id
	 */
	private Long cateGoryId;
	/**
	 * 设计师id
	 */
	private Long designerId;

	/**
	 * 品牌id
	 */
	private String brandId;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 图片
	 */
	private Long picture;
	/**
	 * 原价
	 */
	private BigDecimal originPrice;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 产品状态
	 * 10-现货
	 * 20-预售
	 */
	private Integer state;
	/**
	 * 预售开始时间
	 */
	private Date preSellStartTime;
	/**
	 * 预售结束时间
	 */
	private Date preSellEndTime;
	/**
	 * 是否新品
	 * 0-否
	 * 1-是
	 */
	private Integer isNew;
	/**
	 * 是否为代表作
	 * 0-否
	 * 1-是
	 */
	private Integer isRepresentative;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 产品名称
	 */
	private String productName;
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
	 * 启用状态
	 */
	private Integer isEnable;

	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;


}

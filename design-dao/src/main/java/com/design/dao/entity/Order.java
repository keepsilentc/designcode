package com.design.dao.entity;

import java.math.BigDecimal;


//订单表实体
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Order extends BaseEntity implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8157946049715771421L;
	/**
	 * 用户号
	 */
	private String userNo;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 原订单号,预售商品第一次付款单号
	 */
	private String preOrderNo;
	/**
	 * 订单类型
	 */
	private Integer orderType;
	/**
	 * 支付方式id
	 */
	private String payId;
	/**
	 * 货币id
	 */
	private String currencyId;
	/**
	 * 订单总金额
	 */
	private BigDecimal orderMoney;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 优惠码
	 */
	private String couponNo;
	/**
	 * 折扣金额
	 */
	private BigDecimal discountMoney;
	/**
	 * 物流单号
	 */
	private String deliverNo;
	/**
	 * 物流名称
	 */
	private String deliverName;
	/**
	 * 订单状态
	 * 10-未付款
	 * 20-已完成
	 * 21-已付款
	 * 22-已付定金
	 * 30-已发货
	 * 40-已完成
	 */
	private Integer orderState;
	/**
	 * 支付宝交易号
	 */
	private String tradeNo;
	/**
	 * 地址json
	 */
	private String address;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 是否显示
	 */
	private Integer isShow;
	/**
	 * 乐观锁
	 */
	private Integer modifyCount;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}

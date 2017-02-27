package com.design.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class OrderDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 429642153115069837L;
	private Long id;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 商品编号
	 */
	private String productNo;
	/**
	 * 产品尺寸颜色id
	 */
	private Long ptstId;
	/**
	 * 产品状态
	 * 10-现货
	 * 20-预售
	 */
	private Integer productState;
	/**
	 * 产品购买数量
	 */
	private Integer ptBuyNum;
	/**
	 * 币种
	 */
	private String currencyId;
	/**
	 * 产品总额
	 */
	private BigDecimal ptsumMoney;
	
	/**
	 * 订单详细状态
	 * 10、退款中
	 * 20、退款成功
	 * 40、退款失败
	 * 
	 * 11、换货中
	 * 21、换货成功
	 * 41、换货失败
	 * 
	 * 12、退货中
	 * 22、退货成功
	 * 42、退货失败
	 * 
	 * 50、关闭
	 */
	private Integer orderDetailState;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 原订单号
	 */
	private String preOrderNo;
	/**
	 * 乐观锁
	 */
	private Integer modifyCount;
	
}

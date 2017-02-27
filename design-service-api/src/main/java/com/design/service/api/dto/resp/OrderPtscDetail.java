package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderPtscDetail {
	/**
	 * 订单详细id
	 */
	private String orderDetailId;
	/**
	 * 产品尺寸颜色id
	 */
	private String ptstId;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 颜色名称
	 */
	private String colorName;
	/**
	 * 尺码名称
	 */
	private String sizeName;
	/**
	 * 图片
	 */
	private String picture;
	/**
	 * 颜色图片
	 */
	private String colorPicture;
	/**
	 * 购买数量
	 */
	private String ptBuyNum;
	/**
	 * 币种
	 */
	private String currencyId;
	/**
	 * 币种符号
	 */
	private String currencySign;
	/**
	 * 单价
	 */
	private String price;
	/**
	 * 产品总额
	 */
	private String ptsumMoney;
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
	private String orderDetailState;
	
}

package com.design.service.impl.refund;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.design.common.enums.RefundType;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.Refund;
import com.design.dao.entity.RefundLog;
import com.design.dao.entity.Returns;
import com.design.service.api.dto.TradeMoneyRemainDto;

@Getter
@Setter
public class RefundDto{
	/**
	 * 退款个数
	 */
	private Integer refundNum;
	/**
	 * 退款金额
	 */
	private BigDecimal refundMoney;
	/**
	 * 退款日志
	 */
	private RefundLog refundLog;
	/**
	 * 订单信息
	 */
	private Order order;
	/**
	 * 订单明细信息
	 */
	private OrderDetail orderDetail;
	/**
	 * 退款原因
	 */
	private String reason;
	/**
	 * 附件
	 */
	private String pictures;
	/**
	 * 退款号
	 */
	private String refundNo;
	/**
	 * 退款类型
	 */
	private RefundType refundType;
	
	private boolean canClose;
	
	/**
	 * 订单号对应的交易dto
	 */
	private TradeMoneyRemainDto tmRdto;
	/**
	 * 预售商品 拆单前交易的dto
	 */
	private TradeMoneyRemainDto preOrderTmRdto;
	
	/**
	 * 退款
	 */
	private Refund refund;
	/**
	 * 退货
	 */
	private Returns returns;
	
	private int guard;
}

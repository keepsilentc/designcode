package com.design.service.impl.pay;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonNotify {
	//订单号
	protected String out_trade_no;
	//交易号
	protected String trade_no;
	//支付时间
	protected Date pay_time;
	//交易类型
	protected String pay_type;
	//订单总金额
	protected String total_amount;
	//币种
	protected String currency_id;
	
}

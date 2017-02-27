package com.design.service.impl.pay.paypal;

import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.service.impl.pay.CommonNotify;

@Getter
@Setter
@ToString
public class PayPalNotify extends CommonNotify{
	/**
	 * 收款人email
	 */
	private String receiverEmail;
	/**
	 * 付款人email
	 */
	private String payerEmail;
	/**
	 * 交易状态
	 */
	private String paymentStatus;
	
	public static PayPalNotify parse(Map<String, String> params) {
		return new PayPalNotify().build(params);
	}
	
	private PayPalNotify build(Map<String, String> params) {
		this.pay_type = "PAYPAL";
		this.currency_id = params.get("mc_currency");
		this.total_amount = params.get("mc_gross");
		this.pay_time = new Date();
		this.out_trade_no = params.get("item_name1");
		this.trade_no = params.get("txn_id");
		this.paymentStatus = params.get("payment_status");
		return this;
	}
	
	
}

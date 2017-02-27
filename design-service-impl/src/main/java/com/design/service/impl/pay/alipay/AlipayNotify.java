package com.design.service.impl.pay.alipay;

import java.util.Map;

import com.design.common.utils.DateUtil;
import com.design.service.impl.pay.CommonNotify;

import lombok.Getter;

@Getter
public class AlipayNotify extends CommonNotify{
	private String receipt_amount;
	private String seller_id;
	private String app_id;
	
	public static AlipayNotify parse(Map<String, String> params) {
		return new AlipayNotify().build(params);
	}
	private AlipayNotify build(Map<String, String> params) {
		this.pay_type = "ALIPAY";
		this.currency_id = "CNY";
		this.out_trade_no = params.get("out_trade_no");
		this.trade_no = params.get("trade_no");
		this.total_amount = params.get("total_amount");
		this.receipt_amount = params.get("total_amount");
		this.pay_time = DateUtil.parse(params.get("notify_time"), DateUtil.allPattern);
		this.seller_id = params.get("seller_id");
		this.app_id = params.get("app_id");
		return this;
	}
	
	
}

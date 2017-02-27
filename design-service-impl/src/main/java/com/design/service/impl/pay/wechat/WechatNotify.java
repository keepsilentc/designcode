package com.design.service.impl.pay.wechat;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.common.utils.DateUtil;
import com.design.service.impl.pay.CommonNotify;

@Getter
@Setter
@ToString
public class WechatNotify extends CommonNotify{
	public static WechatNotify parse(Map<String, String> params) {
		return new WechatNotify().build(params);
	}
	private WechatNotify build(Map<String, String> params) {
		this.pay_type = "WECHAT";
		this.currency_id = "CNY";
		this.out_trade_no = params.get("out_trade_no");
		this.trade_no = params.get("transaction_id");
		this.total_amount = String.valueOf(Float.valueOf(params.get("total_fee"))/100f);
		this.pay_time = DateUtil.parse(params.get("time_end"), DateUtil.fullPattern);
		return this;
	}
}

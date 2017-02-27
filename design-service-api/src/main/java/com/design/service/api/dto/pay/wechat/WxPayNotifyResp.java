package com.design.service.api.dto.pay.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxPayNotifyResp extends WxCommonServiceResp{
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String openid;
	private String trade_type;
	private String bank_type;
	private String total_fee;
	private String cash_fee;
	private String transaction_id;
	private String out_trade_no;
	private String time_end;
	
}

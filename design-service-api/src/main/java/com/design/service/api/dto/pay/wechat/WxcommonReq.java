package com.design.service.api.dto.pay.wechat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxcommonReq<T extends WxcommonResp> {
	//商户订单号
	private String out_trade_no;
	//应用ID
	private String appid;
	//商户号
	private String mch_id;
	//随机字符串
	private String nonce_str;
	//签名
	private String sign;
}

package com.design.service.api.dto.pay.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxUnifiedOrderResp extends WxCommonServiceResp{
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String prepay_id;
	private String trade_type;
	
}

package com.design.service.api.dto.pay.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("xml")
public class WxUnifiedOrderReq extends WxcommonReq<WxUnifiedOrderResp>{
	//交易类型(必填)
	private String trade_type;
	//商品描述(必填)
	private String body;
	//通知地址(必填)
	private String notify_url;
	//总金额(必填)(单位为分)
	private Integer total_fee;
	//终端IP(必填)
	private String spbill_create_ip;
	
}

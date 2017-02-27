package com.design.service.api.dto.pay.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxQueryReq extends WxcommonReq<WxQueryResp> {

	//微信订单号
	private String transaction_id;

}

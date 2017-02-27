package com.design.service.api.dto.pay.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxQueryResp extends WxCommonServiceResp{
	private String trade_state;
}

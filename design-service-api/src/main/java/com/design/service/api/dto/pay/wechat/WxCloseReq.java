package com.design.service.api.dto.pay.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxCloseReq extends WxcommonReq<WxCloseResp> {


}

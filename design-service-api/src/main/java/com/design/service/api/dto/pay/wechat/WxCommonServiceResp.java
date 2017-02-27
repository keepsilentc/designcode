package com.design.service.api.dto.pay.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WxCommonServiceResp extends WxcommonResp{
	private String result_code;
	private String err_code;
	private String err_code_des;
}

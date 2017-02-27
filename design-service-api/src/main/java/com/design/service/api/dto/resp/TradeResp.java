package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeResp {
	private boolean isSuccess;
	private String subCode;
	private String subMsg;
}

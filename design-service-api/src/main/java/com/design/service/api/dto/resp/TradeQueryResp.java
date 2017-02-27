package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeQueryResp {
	private String tradeNo;
	private String orderNo;
	private boolean canClose;
}

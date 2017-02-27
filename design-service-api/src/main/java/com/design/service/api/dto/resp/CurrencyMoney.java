package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyMoney {
	private String currencyId;
	private String money;
	private String currencySign;
}

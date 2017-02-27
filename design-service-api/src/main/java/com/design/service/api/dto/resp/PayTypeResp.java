package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayTypeResp {
	private String payId;
	private String payName;
	private String payIcon;
}

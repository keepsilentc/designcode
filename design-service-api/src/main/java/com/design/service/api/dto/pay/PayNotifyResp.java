package com.design.service.api.dto.pay;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class PayNotifyResp {
	private String return_code;
	private String return_msg;
}

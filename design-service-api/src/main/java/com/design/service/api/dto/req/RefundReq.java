package com.design.service.api.dto.req;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString
public class RefundReq {
	
	@NotBlank(message="退款类型不能为空")
	private String refundType;
	
	@NotNull(message="退款请求号不能为空")
	private String refundNo;
	
	@NotNull(message="签名不能为空")
	private String sign;
	
	@NotNull(message="nonce不能为空")
	private String nonce_str;
	
	@NotNull(message="key不能为空")
	private String key;
}

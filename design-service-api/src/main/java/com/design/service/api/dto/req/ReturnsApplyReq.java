package com.design.service.api.dto.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReturnsApplyReq {
	
	@NotNull(message="退换货类型不能为空")
	private Integer returnType;
	
	@NotNull(message="订单详细id不能为空")
	private Long orderDetailId;
	
	@NotNull(message="商品退换货个数不能为空")
	@Min(value=1)
	private Integer returnNum;
	
	private String reason;
	
	private String returnNo;
	
}

package com.design.service.api.dto.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundApplyReq {
	
	@NotNull(message="订单详细id不能为空")
	private Long orderDetailId;
	
	@NotNull(message="商品退款个数不能为空")
	@Min(value=1)
	private Integer refundNum;
	
	private String reason;
	
	private String refundNo;
}

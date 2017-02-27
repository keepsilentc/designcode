package com.design.service.api.dto.req;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;
@Getter
@Setter
@ToString
public class CreateOrderReq {
	
	@Min(value=0,message="订单总金额不能小于0")
	@NotNull(message="订单金额不能为空")
	private BigDecimal orderMoney;
	
	@NotBlank(message="购物车id不能为空")
	private String cartIdsStr;
	
	@NotNull(message="地址id不能为空")
	private Long addressId;
	
	private String couponNo;
	
	private String remark;
	
	private List<Long> cartIds;
}

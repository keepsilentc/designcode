package com.design.service.api.dto.req;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ModifyProductCartReq {
	
	@NotNull(message="购物车记录id不能为空")
	private Long cartId;
	
	@NotNull(message="商品库id不能为空")
	private Long ptscId;
	
	@Min(message="商品数量不能小于0",value=1)
	@NotNull(message="商品数量不能为空")
	private Integer productNum;
}

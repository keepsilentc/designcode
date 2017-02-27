package com.design.service.api.dto.req;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class AddProduct2CartReq {
	
	
	@Min(message="商品数量不能小于1",value=1)
	@NotNull(message="商品数量不能为空")
	private Integer productNum;
	
	@NotNull(message="尺寸id不能为空")
	private Long ptscId;
}

package com.design.service.api.dto.req;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ProductListReq {
	private Long designerId;
	private Long themeId;
	private Long categoryId;
	private Integer isNew;
	private Integer state;
	private Integer pageIndex;
	private Integer pageSize;
	private Integer sortRule;
}

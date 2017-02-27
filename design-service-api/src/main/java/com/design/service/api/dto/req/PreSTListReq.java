package com.design.service.api.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreSTListReq {
	private Integer pageIndex;
	private Integer pageSize;
}

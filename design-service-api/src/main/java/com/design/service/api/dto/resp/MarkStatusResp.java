package com.design.service.api.dto.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkStatusResp {
	private String statusId;
//	private String describe;
	private String statusName;
	private String picture;
}

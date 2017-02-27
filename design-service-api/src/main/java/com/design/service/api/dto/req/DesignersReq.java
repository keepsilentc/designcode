package com.design.service.api.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DesignersReq {
	private String countryId;
	private String designerName;
	private String designerId;
}

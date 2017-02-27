package com.design.service.api.dto.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayLoad extends BasePayLoad{
	
	private Integer payLoadType;
	
	private String idntify;
	
}

package com.design.service.api.dto.payload;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasePayLoad {
	private String title;
	private String body;
	private List<String> deviceTokens;
}

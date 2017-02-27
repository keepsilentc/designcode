package com.design.service.api.dto.req;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedBackReq {
	@NotBlank
	private String name;
	@NotBlank
	private String description;
}

package com.design.service.api.dto.resp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class CategoryResp {
	private String categoryId;
	private String categoryName;
	private String picture;
	private List<CategoryResp> subCategorys;
}

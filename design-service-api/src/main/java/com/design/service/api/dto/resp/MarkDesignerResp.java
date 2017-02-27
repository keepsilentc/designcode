package com.design.service.api.dto.resp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkDesignerResp {
	private String designerName;
	private String photo;
	private String designerId;
	private String countryId;
	private String describe;
	private List<DesignerProduct> designerProducts;
}

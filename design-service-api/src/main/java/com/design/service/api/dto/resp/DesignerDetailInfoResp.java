package com.design.service.api.dto.resp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class DesignerDetailInfoResp{
	private String designerId;
	private String countryId;
	private String designerName;
	private String photo;
	private String isFollow;
	private String describe;
	private String followNum;
	private List<DesignProductResp> productList;
	private List<DesignerStatusResp> statusList;
}

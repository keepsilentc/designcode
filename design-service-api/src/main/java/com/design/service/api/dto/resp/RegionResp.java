package com.design.service.api.dto.resp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegionResp {
	private String region;
	private String regionId;
	
	public RegionResp(String region, String regionId) {
		super();
		this.region = region;
		this.regionId = regionId;
	}
	
}

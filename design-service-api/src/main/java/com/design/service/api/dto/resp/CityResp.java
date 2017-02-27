package com.design.service.api.dto.resp;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class CityResp {
	public void setRegions(List<RegionResp> regions) {
		this.regions = regions;
	}
	private String city;
	private String cityId;
	private List<RegionResp> regions;
	
	public CityResp(String city, String cityId) {
		this.city = city;
		this.cityId = cityId;
		this.regions = new ArrayList<RegionResp>();
	}
}

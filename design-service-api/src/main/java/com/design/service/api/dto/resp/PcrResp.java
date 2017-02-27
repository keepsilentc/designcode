package com.design.service.api.dto.resp;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class PcrResp {
	private String provinceId;
	private String province;
	private List<CityResp> citys;
	
	public PcrResp(){}
	
	public PcrResp(String province,String provinceId) {
		this.provinceId = provinceId;
		this.province = province;
		this.citys = new ArrayList<CityResp>();
	}
	
}

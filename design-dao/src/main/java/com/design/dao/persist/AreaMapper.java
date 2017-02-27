package com.design.dao.persist;

import java.util.List;

import com.design.dao.entity.City;
import com.design.dao.entity.Country;
import com.design.dao.entity.Province;
import com.design.dao.entity.Region;
import com.design.dao.entity.dto.PcrInfo;

public interface AreaMapper {
	List<Country> queryAllCountrys();
	
	List<Province> queryAllProvince();
	
	List<City> queryAllCitysByProvinceId(Integer provinceId);
	
	List<Region> queryAllRegionsByCityId(Integer regionId);
	
	List<PcrInfo> queryAllPcr();
}

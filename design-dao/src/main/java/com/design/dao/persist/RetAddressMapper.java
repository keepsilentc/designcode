package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.RetAddress;
import com.design.dao.entity.dto.RetAddressInfo;

public interface RetAddressMapper {
	
	List<RetAddressInfo> getAddressesByUserNo(String userNo);

	void insert(RetAddress address);

	Integer getAddressNumByUserNo(String userNo);

	Integer batchDelete(List<Long> ids);

	Integer updateById(RetAddress address);

	void setDefault(Map<String,Object> map);

	RetAddressInfo getAddressById(@Param(value = "userNo")String userNo,@Param(value = "addressId")Long addressId);

}

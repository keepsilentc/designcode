package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.dto.StatusDetailInfo;
import com.design.dao.entity.dto.StatusInfo;


public interface StatusMapper {

	List<StatusInfo> getStatusList(Map<String, Object> params);

	StatusDetailInfo getStatusDetailInfoById(@Param(value = "id")Long id,@Param(value = "userNo")String userNo);

	List<Map<String, Object>> getAllStatus(Long designerId);

	List<StatusInfo> getMarkStatus(String userNo);

}

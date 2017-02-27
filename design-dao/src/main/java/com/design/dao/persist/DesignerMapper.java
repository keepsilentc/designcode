package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.dto.DesignerInfo;
import com.design.dao.entity.dto.MarkDesignerInfo;

public interface DesignerMapper {

	List<DesignerInfo> getDesignersByCondition(Map<String, String> param);

	DesignerInfo getDesignerDetailInfoById(@Param(value="userNo")String userNo,@Param(value="id")Long id);

	List<DesignerInfo> getDesignersByUserNo(String userNo);

	List<MarkDesignerInfo> getMarkDesigners(String userNo);

}

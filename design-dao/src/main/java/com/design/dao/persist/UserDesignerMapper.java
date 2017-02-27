package com.design.dao.persist;

import java.util.Map;

import com.design.dao.entity.UserDesigner;

public interface UserDesignerMapper {

	Integer getPraiseNumByDesignerId(Long designerId);

	void insert(UserDesigner userDesigner);

	UserDesigner getUserDesignerByCondition(Map<String, Object> params);

	int update(UserDesigner userDesigner);

	Integer getMarkDesignerNum(String userNo);

	int del(UserDesigner t_userDesigner);

}

package com.design.dao.persist;

import java.util.Map;

import com.design.dao.entity.UserStatus;


public interface UserStatusMapper {
	
	void insert(UserStatus userStatus);

	int del(UserStatus t_userStatus);

	UserStatus getUserStatusByCondition(Map<String, Object> params);

	UserStatus getLatestUserByStatusId(Long statusId);

	Integer getMarkStatusNum(String userNo);

}

package com.design.dao.persist;

import java.util.Map;



import com.design.dao.entity.User;
public interface UserMapper {

	public int isUserExist(Map<String, String> map);

	public void insert(User user);

	public User getUserByCondition(Map<String, Object> params);
	
	public int updateUserById(User t_user);

	public void updateDeviceTokenUserById(User t_user);

	public void updatePwd(User t_user);
	
}

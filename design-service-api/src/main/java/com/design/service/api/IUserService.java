package com.design.service.api;

import java.util.Map;

import com.design.dao.entity.User;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.ValidateRegisterReq;



public interface IUserService {
	
	void preRegisterExec(PreRegisterReq preRegisterReq);
	
	void validatRegisterCode(ValidateRegisterReq validateRegisterReq);
	
	void registerExec(RegisterReq registerReq);

	String loginExec(LoginReq loginReq);
	
	User getUserByCondition(Map<String, Object> params);

	void updateUserById(User t_user);

	User getUserByToken(String token);

	User getUserByUserNo(String userNo);
	
	String getUserNoByToken(String token);

	void updateDeviceTokenUserById(User t_user);

	void preResetPwdExec(PreResetPwdReq preResetPwdReq);

	void resetPwdExec(ResetPwdReq resetPwdReq);

}

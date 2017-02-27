package com.design.service.impl.user;

import org.springframework.stereotype.Service;

import com.design.dao.entity.User;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.ValidateRegisterReq;
@Service
public class DefaultUserServiceImpl extends AbstractUserService{

	@Override
	public User login(LoginReq loginReq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void preRegister(PreRegisterReq preRegisterReq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User register(RegisterReq registerReq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void validatRegisterCode(ValidateRegisterReq validateRegisterReq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void preResetPwdExec(PreResetPwdReq preResetPwdReq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resetPwdExec(ResetPwdReq resetPwdReq) {
		throw new UnsupportedOperationException();
	}

}

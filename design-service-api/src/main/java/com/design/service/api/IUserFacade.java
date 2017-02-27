package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.UpdateUserInfoReq;
import com.design.service.api.dto.ValidateRegisterReq;
import com.design.service.api.dto.resp.MarkDesignerResp;
import com.design.service.api.dto.resp.MarkNumberResp;
import com.design.service.api.dto.resp.MarkStatusResp;
import com.design.service.api.dto.resp.SubScribeMailResp;
import com.design.service.api.dto.resp.UserInfoResp;

public interface IUserFacade {
	
	void preRegister(PreRegisterReq preRegisterReq);

	void validatRegisterCode(ValidateRegisterReq validateRegisterReq);
	
	void register(RegisterReq registerReq);

	String login(LoginReq loginReq);

	void logout(String token);

	void refreshToken(String token);

	UserInfoResp getUserInfo(String token);

	void updateUserInfo(String token, UpdateUserInfoReq req);

	MarkNumberResp getMarkNumber(String token);

	void bindDeviceToken(String token, String deviceToken);

	void unbindDeviceToken(String token);

	List<MarkDesignerResp> getMarkDesigners(String token);

	List<MarkStatusResp> getMarkStatus(String token);

	void subScribeMail(String token, Integer isSubscribe, String subScribeMail);

	SubScribeMailResp getScribeMailInfo(String token);

	void preResetPwd(PreResetPwdReq preResetPwdReq);

	void resetPwd(ResetPwdReq resetPwdReq);

	

}

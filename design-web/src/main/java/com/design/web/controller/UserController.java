package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.assist.DesignSubmitException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IUserFacade;
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
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/user")
@Controller
public class UserController {
	@Resource
	private IUserFacade userFacade;
	private static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Response<String> login(LoginReq loginReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("登陆接口开始执行");
			ValidateUtils.validateEntiryThrows(loginReq);
			String token = userFacade.login(loginReq);
			resp.setResult(token);
		}catch(DesignException ex){
			log.info("登陆接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(DesignSubmitException ex){
			log.info("注册接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("登陆接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/validatRegisterCode",method=RequestMethod.POST)
	public Response<Void> validatRegisterCode(ValidateRegisterReq validateRegisterReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("验证注册验证码接口开始执行");
			ValidateUtils.validateEntiryThrows(validateRegisterReq);
			userFacade.validatRegisterCode(validateRegisterReq);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("验证注册验证码接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("验证注册验证码接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/preRegister",method=RequestMethod.POST)
	public Response<Void> preRegister(PreRegisterReq preRegisterReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("注册发送验证码接口开始执行,{}",preRegisterReq);
			ValidateUtils.validateEntiryThrows(preRegisterReq);
			userFacade.preRegister(preRegisterReq);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("注册发送验证码接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("注册发送验证码接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public Response<Void> register(RegisterReq registerReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("注册接口开始执行,{}",registerReq);
			ValidateUtils.validateEntiryThrows(registerReq);
			userFacade.register(registerReq);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("注册接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("注册接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public Response<Void> logout(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("注销接口开始执行");
			userFacade.logout(token);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("注销接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("注销接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/refreshToken",method=RequestMethod.GET)
	public Response<String> refreshToken(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("刷新token接口开始执行");
			userFacade.refreshToken(token);
			resp.setResult(token);
		}catch(DesignException ex){
			log.info("刷新token接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("刷新token接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getUserInfo",method=RequestMethod.GET)
	public Response<UserInfoResp> getUserInfo(@RequestHeader(name="token",required=false)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<UserInfoResp> resp = new Response<UserInfoResp>();
		try{
			log.info("获取用户信息接口开始执行");
			UserInfoResp result = userFacade.getUserInfo(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取用户信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取用户信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/updateUserInfo",method=RequestMethod.POST)
	public Response<Void> updateUserInfo(@RequestHeader(name="token",required=true)String token,UpdateUserInfoReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("修改用户信息接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			userFacade.updateUserInfo(token,req);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("修改用户信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("修改用户信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/bindDeviceToken",method=RequestMethod.POST)
	public Response<Void> bindDeviceToken(@RequestHeader(name="token",required=true)String token,String deviceToken){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("绑定设备token接口开始执行,{}",deviceToken);
			userFacade.bindDeviceToken(token,deviceToken);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("绑定设备token接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("绑定设备token接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/unbindDeviceToken",method=RequestMethod.GET)
	public Response<Void> unbindDeviceToken(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("解绑设备token接口开始执行");
			userFacade.unbindDeviceToken(token);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("解绑设备token接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("解绑设备token接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getMarkNumber",method=RequestMethod.GET)
	public Response<MarkNumberResp> getMarkNumber(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<MarkNumberResp> resp = new Response<MarkNumberResp>();
		try{
			log.info("获取用户关注和收藏数接口开始执行");
			MarkNumberResp markNumber = userFacade.getMarkNumber(token);
			resp.setResult(markNumber);
		}catch(DesignException ex){
			log.info("获取用户关注和收藏数接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取用户关注和收藏数接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getMarkDesigners",method=RequestMethod.GET)
	public Response<List<MarkDesignerResp>> getMarkDesigners(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<MarkDesignerResp>> resp = new Response<List<MarkDesignerResp>>();
		try{
			log.info("获取收藏的设计师列表接口开始执行");
			List<MarkDesignerResp> result = userFacade.getMarkDesigners(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取收藏的设计师列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取收藏的设计师列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getMarkStatus",method=RequestMethod.GET)
	public Response<List<MarkStatusResp>> getMarkStatus(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<MarkStatusResp>> resp = new Response<List<MarkStatusResp>>();
		try{
			log.info("获取收藏的动态列表接口开始执行");
			List<MarkStatusResp> result = userFacade.getMarkStatus(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取收藏的动态列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取收藏的动态列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/subScribeMail",method=RequestMethod.POST)
	public Response<Void> subScribeMail(@RequestHeader(name="token")String token,Integer isSubscribe,String subScribeMail){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("订阅邮件接口开始执行");
			userFacade.subScribeMail(token,isSubscribe,subScribeMail);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("订阅邮件接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("订阅邮件接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getScribeMailInfo",method=RequestMethod.GET)
	public Response<SubScribeMailResp> getScribeMailInfo(@RequestHeader(name="token")String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<SubScribeMailResp> resp = new Response<SubScribeMailResp>();
		try{
			log.info("获取订阅信息接口开始执行");
			SubScribeMailResp result = userFacade.getScribeMailInfo(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取订阅信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取订阅信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/preResetPwd",method=RequestMethod.POST)
	public Response<Void> preResetPwd(PreResetPwdReq preResetPwdReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("重置密码发送验证码接口开始执行,{}",preResetPwdReq);
			ValidateUtils.validateEntiryThrows(preResetPwdReq);
			userFacade.preResetPwd(preResetPwdReq);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("重置密码发送验证码接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("重置密码发送验证码接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/resetPwd",method=RequestMethod.POST)
	public Response<Void> resetPwd(ResetPwdReq resetPwdReq){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("重置密码接口开始执行,{}",resetPwdReq);
			ValidateUtils.validateEntiryThrows(resetPwdReq);
			userFacade.resetPwd(resetPwdReq);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("重置密码接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("重置密码接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

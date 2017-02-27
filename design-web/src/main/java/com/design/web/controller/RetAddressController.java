package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IRetAddressService;
import com.design.service.api.dto.req.AddAddressReq;
import com.design.service.api.dto.req.UpdateAddressReq;
import com.design.service.api.dto.resp.RetAddressResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@Controller
@RequestMapping("/retAddress")
public class RetAddressController {
	private static Logger log = LoggerFactory.getLogger(RetAddressController.class);
	@Resource
	private IRetAddressService retAddressServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getAddressList",method=RequestMethod.GET)
	public Response<List<RetAddressResp>> getAddressList(@RequestHeader(name="token",required=true)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<RetAddressResp>> resp = new Response<List<RetAddressResp>>();
		try{
			log.info("获取用户地址列表接口开始执行");
			List<RetAddressResp> addresses = retAddressServiceImpl.getAddressList(token);
			resp.setResult(addresses);
		}catch(DesignException ex){
			log.info("获取用户地址列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取用户地址列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/addAddress",method=RequestMethod.POST)
	public Response<String> addaddress(@RequestHeader(name="token",required=true)String token,AddAddressReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("添加用户地址接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			String addressNum = retAddressServiceImpl.addAddress(token,req);
			resp.setResult(addressNum);
		}catch(DesignException ex){
			log.info("添加用户地址接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("添加用户地址接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/updateAddress",method=RequestMethod.POST)
	public Response<Void> updateAddress(@RequestHeader(name="token",required=true)String token,UpdateAddressReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("编辑用户地址接口开始执行,{}",req);
			retAddressServiceImpl.updateAddress(token,req);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("编辑用户地址接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("编辑用户地址接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/removeAddress",method=RequestMethod.POST)
	public Response<String> removeAddress(@RequestHeader(name="token",required=true)String token,@RequestParam List<Long> addressId){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("删除用户地址接口开始执行");
			String addressNum = retAddressServiceImpl.removeAddress(token,addressId);
			resp.setResult(addressNum);
		}catch(DesignException ex){
			log.info("删除用户地址接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("删除用户地址接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/setDefault",method=RequestMethod.POST)
	public Response<String> setDefault(@RequestHeader(name="token",required=true)String token,@RequestParam @NotNull Long id){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("设置用户默认收货地址接口开始执行");
			retAddressServiceImpl.setDefault(token,id);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("设置用户默认收货地址接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("设置用户默认收货地址接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

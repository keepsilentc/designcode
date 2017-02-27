package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.service.api.ICommonService;
import com.design.service.api.dto.resp.CarouselResp;
import com.design.service.api.dto.resp.CountryResp;
import com.design.service.api.dto.resp.PayTypeResp;
import com.design.service.api.dto.resp.PcrResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;
@RequestMapping("/common")
@Controller
public class CommonController {
	private static Logger log = LoggerFactory.getLogger(CommonController.class);
	
	@Resource
	private ICommonService commonServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getCarouselList",method=RequestMethod.GET)
	public Response<List<CarouselResp>> getCarouselList(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CarouselResp>> resp = new Response<List<CarouselResp>>();
		try{
			log.info("获取轮播图列表接口开始执行");
			List<CarouselResp> payTypes = commonServiceImpl.getCarouselList();
			resp.setResult(payTypes);
		}catch(DesignException ex){
			log.info("获取轮播图列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取轮播图列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getPayList",method=RequestMethod.GET)
	public Response<List<PayTypeResp>> getPayList(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<PayTypeResp>> resp = new Response<List<PayTypeResp>>();
		try{
			log.info("查询支付方式列表接口开始执行");
			List<PayTypeResp> payTypes = commonServiceImpl.getPayList();
			resp.setResult(payTypes);
		}catch(DesignException ex){
			log.info("查询支付方式列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询支付方式列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getAllcountrys",method=RequestMethod.GET)
	public Response<List<CountryResp>> getAllcountrys(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CountryResp>> resp = new Response<List<CountryResp>>();
		try{
			log.info("获取所有国家接口开始执行");
			List<CountryResp> result = commonServiceImpl.getAllcountrys();
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取所有国家接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取所有国家接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getPCR",method=RequestMethod.GET)
	public Response<List<PcrResp>> getPcrs(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<PcrResp>> resp = new Response<List<PcrResp>>();
		try{
			log.info("获取国内省市区接口开始执行");
			List<PcrResp> result = commonServiceImpl.getPcrs();
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取国内省市区接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取国内省市区接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

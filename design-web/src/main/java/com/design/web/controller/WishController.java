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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IWishService;
import com.design.service.api.dto.req.WishListReq;
import com.design.service.api.dto.resp.WishListResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/wish")
@Controller
public class WishController {
	
	private static Logger log = LoggerFactory.getLogger(WishController.class);
	
	@Resource
	private IWishService wishServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/addWish",method=RequestMethod.POST)
	public Response<Void> addWish(@RequestHeader String token,@RequestParam Long ptscId){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("新增愿望清单接口开始执行");
			wishServiceImpl.addWish(token,ptscId);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("新增愿望清单接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("新增愿望清单接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteWish",method=RequestMethod.POST)
	public Response<Void> deleteWish(@RequestHeader String token,@RequestParam Long wishId){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("删除愿望清单接口开始执行");
			wishServiceImpl.deleteWish(token,wishId);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("删除愿望清单接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("删除愿望清单接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getWishs",method=RequestMethod.POST)
	public Response<List<WishListResp>> getWishs(@RequestHeader String token,WishListReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<WishListResp>> resp = new Response<List<WishListResp>>();
		try{
			log.info("查询愿望清单列表接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			List<WishListResp> wishs = wishServiceImpl.getWishs(token,req);
			resp.setResult(wishs);
		}catch(DesignException ex){
			log.info("查询愿望清单列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询愿望清单列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/wishToCart",method=RequestMethod.POST)
	public Response<Void> wishToCart(@RequestHeader String token,@RequestParam Long wishId){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("愿望清单加入购物车接口开始执行");
			wishServiceImpl.wishToCart(token,wishId);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("愿望清单加入购物车接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("愿望清单加入购物车接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

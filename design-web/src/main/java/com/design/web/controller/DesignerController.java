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
import com.design.service.api.IDesignerService;
import com.design.service.api.dto.req.DesignersReq;
import com.design.service.api.dto.resp.DesignerDetailInfoResp;
import com.design.service.api.dto.resp.DesignersResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@Controller
@RequestMapping("/designer")
public class DesignerController {
	private static Logger log = LoggerFactory.getLogger(DesignerController.class);
	
	@Resource
	private IDesignerService designerServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/searchDesigners",method=RequestMethod.POST)
	public Response<List<DesignersResp>> searchDesigners(DesignersReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<DesignersResp>> resp = new Response<List<DesignersResp>>();
		try{
			log.info("查询所有设计师接口开始执行");
			List<DesignersResp> result = designerServiceImpl.searchDesigners(req);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询所有设计师接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询所有设计师接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getDesigners",method=RequestMethod.GET)
	public Response<List<DesignersResp>> getDesigners(@RequestHeader(value="token",required=false)String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<DesignersResp>> resp = new Response<List<DesignersResp>>();
		try{
			log.info("查询所有设计师接口开始执行");
			List<DesignersResp> result = designerServiceImpl.getDesigners(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询所有设计师接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询所有设计师接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getDesignInfo",method=RequestMethod.POST)
	public Response<DesignerDetailInfoResp> getDesignInfo(@RequestHeader(value="token",required=false)String token,@RequestParam("id") Long id){
		TraceLogIdUtils.setTraceLogId(null);
		Response<DesignerDetailInfoResp> resp = new Response<DesignerDetailInfoResp>();
		try{
			log.info("查询设计师详细信息接口开始执行");
			DesignerDetailInfoResp result = designerServiceImpl.getDesignInfo(token,id);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询设计师详细信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询设计师详细信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/follow",method=RequestMethod.POST)
	public Response<Void> follow(@RequestHeader String token,@RequestParam Long designerId,@RequestParam Integer follow){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("关注设计师接口开始执行");
			designerServiceImpl.follow(token,designerId,follow);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("关注设计师接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("关注设计师接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

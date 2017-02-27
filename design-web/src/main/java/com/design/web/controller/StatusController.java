package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import com.design.service.api.IStatusService;
import com.design.service.api.dto.req.StatusReq;
import com.design.service.api.dto.resp.StatusDetailResp;
import com.design.service.api.dto.resp.StatusListResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/status")
@Controller
public class StatusController {
	private static Logger log = LoggerFactory.getLogger(StatusController.class);
	@Resource
	private IStatusService statusServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getStatusList",method=RequestMethod.POST)
	public Response<List<StatusListResp>> getStatusList(@RequestHeader(name="token",required=false) String token,StatusReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<StatusListResp>> resp = new Response<List<StatusListResp>>();
		try{
			log.info("查询动态列表接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			List<StatusListResp> statusList = statusServiceImpl.getStatusList(token,req);
			resp.setResult(statusList);
		}catch(DesignException ex){
			log.info("查询动态列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询动态列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getStatusDetail",method=RequestMethod.POST)
	public Response<StatusDetailResp> getStatusDetail(@RequestHeader(name="token",required=false) String token,@RequestParam Long id){
		TraceLogIdUtils.setTraceLogId(null);
		Response<StatusDetailResp> resp = new Response<StatusDetailResp>();
		try{
			log.info("查询动态详情接口开始执行");
			StatusDetailResp statusDetail = statusServiceImpl.getStatusDetail(token,id);
			resp.setResult(statusDetail);
		}catch(DesignException ex){
			log.info("查询动态详情接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询动态详情接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getShareContent",method=RequestMethod.GET)
	public void getStatusDetail(HttpServletResponse response,@RequestParam Long statusId){
		TraceLogIdUtils.setTraceLogId(null);
		String htmlStr = null;
		try{
			log.info("分享动态详情接口开始执行");
			response.setContentType("text/html; charset=UTF-8");
			StringBuilder builder = new StringBuilder("<html><head><title></title><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /><style>img{width:100%;}</style></head><body>");
			builder.append(statusServiceImpl.getShareContent(statusId));
			builder.append("</body></html>");
			htmlStr = builder.toString();
			response.getOutputStream().write(htmlStr.getBytes("UTF-8"));
			response.getOutputStream().flush();
		}catch(DesignException ex){
			log.info("分享动态详情接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
		}catch(Exception e){
			log.info("分享动态详情接口执行异常,{}",Throwables.getStackTraceAsString(e));
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/markStatus",method=RequestMethod.POST)
	public Response<Void> markStatus(@RequestHeader String token,@RequestParam Long statusId,@RequestParam Integer mark){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("点赞动态接口开始执行");
			statusServiceImpl.markStatus(token,statusId,mark);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("点赞动态接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("点赞动态接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

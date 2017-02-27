package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IFeedbackService;
import com.design.service.api.dto.req.FeedBackReq;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/feedback")
@Controller
public class FeedBackController {
	
	private static Logger log = LoggerFactory.getLogger(FeedBackController.class);
	@Resource
	private IFeedbackService feedbackServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/feedBack",method=RequestMethod.POST)
	public Response<Void> feedBack(@RequestHeader(name="token",required=true)String token,FeedBackReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("提交用户反馈接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			feedbackServiceImpl.feedBack(token,req);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("提交用户反馈接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("提交用户反馈接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

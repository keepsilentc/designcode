package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.service.api.IStaticService;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/static")
@Controller
public class StaticInfoController {
	private static Logger log = LoggerFactory.getLogger(StaticInfoController.class);
	
	@Resource
	private IStaticService staticServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getPrivateItems",method=RequestMethod.GET)
	public Response<String> getPrivateItems(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("获取隐私URL接口开始执行");
			String url = staticServiceImpl.getPrivateItems();
			resp.setResult(url);
		}catch(DesignException ex){
			log.info("获取隐私URL接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取隐私URL接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

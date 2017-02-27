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
import com.design.service.api.ISizeService;
import com.design.service.api.dto.resp.SizeTypeResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;
@RequestMapping("/size")
@Controller
public class SizeController {
	private static Logger log = LoggerFactory.getLogger(SizeController.class);
	@Resource
	private ISizeService sizeService;
	@ResponseBody
	@RequestMapping(value="/sizeTypes",method=RequestMethod.GET)
	public Response<List<SizeTypeResp>> getAllSizeType(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<SizeTypeResp>> resp = new Response<List<SizeTypeResp>>();
		try{
			log.info("查询尺寸类型接口开始执行");
			List<SizeTypeResp> result = sizeService.getAllSizeType();
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询尺寸类型接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询尺寸类型接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

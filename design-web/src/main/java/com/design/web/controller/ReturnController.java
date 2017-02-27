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
import org.springframework.web.multipart.MultipartFile;

import com.design.common.assist.DesignException;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IReturnService;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.req.ReturnsApplyReq;
import com.design.service.impl.user.UploadServiceImpl;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/return")
@Controller
public class ReturnController {
	
	private static Logger log = LoggerFactory.getLogger(ReturnController.class);
	
	@Resource
	private IReturnService returnServiceImpl;
	@Resource
	private UploadServiceImpl uploadServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/returnApply",method=RequestMethod.POST)
	public Response<Void> returnApply(@RequestHeader("token")String token,ReturnsApplyReq req,@RequestParam(required=false) MultipartFile[] pictures){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("退换货申请接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			List<AttachmentDto> attachmentDtos = null;
			if(pictures!=null){
				attachmentDtos = uploadServiceImpl.uploadFile(pictures,"return");
			}
			
			try {
				returnServiceImpl.returnsApply(token,req,attachmentDtos);
			} catch (Exception e) {
				if(CollectionUtils.isNotEmpty(attachmentDtos)){
					uploadServiceImpl.removeFile(attachmentDtos);
				}
				throw e;
			}
			
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("退换货申请接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("退换货申请接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	
}

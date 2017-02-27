package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.service.impl.user.UploadServiceImpl;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;
@Controller
@RequestMapping("/file")
public class FileController {
	
	private static Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Resource
	private UploadServiceImpl uploadServiceImpl;
	
	
	
	@RequestMapping("/get")
	@ResponseBody
	public void getFile(HttpServletResponse resp,@RequestParam(value="id",required=true) Long id){
		TraceLogIdUtils.setTraceLogId(null);
		if(id==null){
			return;
		}
		try {
			File file = uploadServiceImpl.getFile(id);
//			resp.setHeader("Content-Encoding", "gzip");
			FileUtils.copyFile(file, resp.getOutputStream());
//			FileUtils.copyFile(file, new GZIPOutputStream(resp.getOutputStream()));
		} catch (Exception e) {
			log.info(Throwables.getStackTraceAsString(e));
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/uploadAvatar",method=RequestMethod.POST)
	public Response<Void> uploadavatar(@RequestHeader String token,@RequestParam("file") CommonsMultipartFile file){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("上传用户头像接口开始执行");
			uploadServiceImpl.uploadavatar(token,file);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("上传用户头像接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("上传用户头像接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

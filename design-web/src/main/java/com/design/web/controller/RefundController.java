package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.SignUtil;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.IRefundService;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.req.RefundApplyReq;
import com.design.service.api.dto.req.RefundReq;
import com.design.service.api.dto.resp.RefundFollowResp;
import com.design.service.impl.user.UploadServiceImpl;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@RequestMapping("/refund")
@Controller
public class RefundController {
	
	private static Logger log = LoggerFactory.getLogger(RefundController.class);
	
	@Resource
	private IRefundService refundServiceImpl;
	@Resource
	private UploadServiceImpl uploadServiceImpl;
	
	@Value("${sign.key}")
	private String signKey;
	
	
	@ResponseBody
	@RequestMapping(value="/refundApply",method=RequestMethod.POST)
	public Response<Void> refundApply(@RequestHeader String token,RefundApplyReq req,@RequestParam(required=false) MultipartFile[] pictures){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("申请退款接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			List<AttachmentDto> attachmentDtos = null;
			if(pictures!=null){
				attachmentDtos = uploadServiceImpl.uploadFile(pictures,"refund");
			}
			
			try {
				refundServiceImpl.refundApply(token,req,attachmentDtos);
			} catch (Exception e) {
				if(CollectionUtils.isNotEmpty(attachmentDtos)){
					uploadServiceImpl.removeFile(attachmentDtos);
				}
				throw e;
			}
			
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("申请退款接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("申请退款接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/refund",method=RequestMethod.POST)
	public Response<Void> refund(RefundReq refundReq,HttpServletRequest request){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("退款接口开始执行,{}",refundReq);
			ValidateUtils.validateEntiryThrows(refundReq);
			StringBuffer url = request.getRequestURL();
			url.delete(url.length()-request.getRequestURI().length(), url.length());
			log.info("退款url,{}",url);
			if(refundReq.getKey().equals("@^DEsignZxkj")){
				Map<String,String> params = Maps.newHashMap();
				params.put("refundType",refundReq.getRefundType());
				params.put("refundNo", refundReq.getRefundNo());
				params.put("nonce_str", refundReq.getNonce_str());
				params.put("key", refundReq.getKey());
				
				String sign = SignUtil.sign(params);
				if(sign.equals(refundReq.getSign())){
					refundServiceImpl.refund(refundReq);
					resp.setResult(null);
				}else{
					resp.setRespMessage("签名验证失败");
				}
			}else{
				resp.setRespMessage("key验证失败");
			}
			
		}catch(DesignException ex){
			log.info("退款接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("退款接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/follow",method=RequestMethod.POST)
	private Response<List<RefundFollowResp>> follow(@RequestParam Long orderDetailId){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<RefundFollowResp>> resp = new Response<List<RefundFollowResp>>();
		try{
			log.info("跟踪退款接口执行...{}",orderDetailId);
			List<RefundFollowResp> refundLogList = refundServiceImpl.follow(orderDetailId);
			resp.setResult(refundLogList);
		}catch(DesignException ex){
			log.info("跟踪退款接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("跟踪退款接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(DesignEx.INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(DesignEx.INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

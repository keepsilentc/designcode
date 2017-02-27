package com.design.web.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.service.api.dto.pay.PayNotifyResp;
import com.design.service.impl.pay.alipay.AliPayServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/alipay")
public class AliPayController {
	
	private static Logger log = LoggerFactory.getLogger(AliPayController.class);
	@Resource
	private AliPayServiceImpl aliPayServiceImpl;
	
//	@ResponseBody
//	@RequestMapping(value="/preCreate",method=RequestMethod.POST)
//	public Response<String> preCreate(String orderNo,BigDecimal totalAmount){
//		TraceLogIdUtils.setTraceLogId(null);
//		Response<String> resp = new Response<String>();
//		try{
//			log.info("支付宝订单预创建接口开始执行");
//			AlipayClient client = alipayUtil.getClient();
//			AlipayTradePrecreateRequest preCreateReq  =  new AlipayTradePrecreateRequest();
//			AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
//			model.setOutTradeNo(orderNo);
//			model.setTotalAmount(totalAmount.toString());
//			model.setSubject("尺寸");
//			preCreateReq.setBizModel(model);
//			AlipayTradePrecreateResponse preCreateResp = client.execute(preCreateReq);
//			client.sdkExecute(preCreateReq);
//			resp.setResult(preCreateResp.getBody());
//		}catch(DesignException ex){
//			log.info("支付宝订单预创建接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
//			resp.setRespCode(ex.getErrCode());
//			resp.setRespMessage(ex.getMessage());
//		}catch(Exception e){
//			log.info("支付宝订单预创建接口执行异常,{}",Throwables.getStackTraceAsString(e));
//			resp.setRespCode(INTERNAL_ERROR.getErrCode());
//			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
//		}
//		return resp;
//	}
	
	@ResponseBody
	@RequestMapping(value="/notify")
	public String alipayNotify(HttpServletRequest req,HttpServletResponse resp){
		TraceLogIdUtils.setTraceLogId(null);
		try{
			Map<String,String> params = Maps.newHashMap();
			Enumeration<String> paramNames =  req.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				params.put(paramName, req.getParameter(paramName));
			}
			log.info("支付宝异步通知回掉接口开始执行...{}",params);
			PayNotifyResp payNotifyResp = aliPayServiceImpl.payNotify(params);
			return payNotifyResp.getReturn_code();
		}catch(DesignException ex){
			log.info("支付宝异步通知回掉接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			return Constant.FAIL;
		}catch(Exception e){
			log.info("支付宝异步通知回掉接口执行异常,{}",Throwables.getStackTraceAsString(e));
			return Constant.FAIL;
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/test")
	public void test(String userNo,String orderNo){
		TraceLogIdUtils.setTraceLogId(null);
		try{
			aliPayServiceImpl.test(userNo,orderNo);
		}catch(DesignException ex){
			log.info("支付宝异步通知回掉接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
		}catch(Exception e){
			log.info("支付宝异步通知回掉接口执行异常,{}",Throwables.getStackTraceAsString(e));
		}
	}
	
}

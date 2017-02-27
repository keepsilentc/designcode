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
import com.design.service.impl.pay.paypal.PayPalServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/paypal")
public class PaypalController {
	
	private static Logger log = LoggerFactory.getLogger(PaypalController.class);
	
	@Resource
	private PayPalServiceImpl payPalServiceImpl;
	
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
			log.info("paypal及时付款通知回掉接口开始执行...{}",params);
			PayNotifyResp payNotifyResp = payPalServiceImpl.payNotify(params);
			return payNotifyResp.getReturn_code();
		}catch(DesignException ex){
			log.info("paypal及时付款通知回掉接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			return Constant.FAIL;
		}catch(Exception e){
			log.info("paypal及时付款通知回掉接口执行异常,{}",Throwables.getStackTraceAsString(e));
			return Constant.FAIL;
		}
	}
	
}

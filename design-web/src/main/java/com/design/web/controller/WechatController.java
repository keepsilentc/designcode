package com.design.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import com.design.service.impl.pay.wechat.WeiXinPayServiceImpl;
import com.design.service.impl.pay.wechat.WxXmlUtil;
import com.google.common.base.Throwables;

@Controller
@RequestMapping("/wechat")
public class WechatController {
	
	private static Logger log = LoggerFactory.getLogger(WechatController.class);
	
	@Resource
	private  WeiXinPayServiceImpl weiXinPayServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/notify")
	public String alipayNotify(HttpServletRequest req,HttpServletResponse resp){
		TraceLogIdUtils.setTraceLogId(null);
		PayNotifyResp result = new PayNotifyResp();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String temp = null;
			while((temp=reader.readLine())!=null){
				builder.append(temp);
				builder.append("\n");
			}
			log.info("微信异步通知回掉接口开始执行...{}",builder.toString());
			result = weiXinPayServiceImpl.wxNotify(builder.toString());
		}catch(DesignException ex){
			log.info("微信异步通知回掉接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			result.setReturn_code(Constant.FAIL);
			result.setReturn_msg(ex.getMessage());
		}catch(Exception e){
			log.info("微信异步通知回掉接口执行异常,{}",Throwables.getStackTraceAsString(e));
			result.setReturn_code(Constant.FAIL);
		}
		return WxXmlUtil.toXml(result);
	}
}

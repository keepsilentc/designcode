package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.validator.constraints.NotBlank;
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
import com.design.service.api.IOrderService;
import com.design.service.api.dto.req.CreateOrderReq;
import com.design.service.api.dto.req.OrderListReq;
import com.design.service.api.dto.req.SignReq;
import com.design.service.api.dto.resp.OrderDetailResp;
import com.design.service.api.dto.resp.OrderListResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);
	@Resource
	private IOrderService orderServiceImpl;
	
	
	@ResponseBody
	@RequestMapping(value="/createOrder",method=RequestMethod.POST)
	public Response<String> createOrder(@RequestHeader(name="token",required=true)String token,CreateOrderReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("生成订单接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			String orderNo = orderServiceImpl.createOrder(token,req);
			resp.setResult(orderNo);
		}catch(DesignException ex){
			log.info("生成订单接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("生成订单接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getOrders",method=RequestMethod.POST)
	public Response<List<OrderListResp>> getOrders(@RequestHeader(name="token",required=true)String token,OrderListReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<OrderListResp>> resp = new Response<List<OrderListResp>>();
		try{
			log.info("获取订单列表接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			List<OrderListResp> orders = orderServiceImpl.getOrders(token,req);
			resp.setResult(orders);
		}catch(DesignException ex){
			log.info("获取订单列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取订单列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/getRefundOrders",method=RequestMethod.GET)
//	public Response<List<OrderListResp>> getRefundOrders(@RequestHeader(name="token",required=true)String token){
//		TraceLogIdUtils.setTraceLogId(null);
//		Response<List<OrderListResp>> resp = new Response<List<OrderListResp>>();
//		try{
//			log.info("获取退款订单列表接口开始执行");
//			List<OrderListResp> orders = orderServiceImpl.getRefundOrders(token);
//			resp.setResult(orders);
//		}catch(DesignException ex){
//			log.info("获取退款订单列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
//			resp.setRespCode(ex.getErrCode());
//			resp.setRespMessage(ex.getMessage());
//		}catch(Exception e){
//			log.info("获取退款订单列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
//			resp.setRespCode(INTERNAL_ERROR.getErrCode());
//			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
//		}
//		return resp;
//	}
	
	@ResponseBody
	@RequestMapping(value="/getOrderDetail",method=RequestMethod.POST)
	public Response<OrderDetailResp> getOrderDetail(@RequestHeader(name="token",required=true)String token,@RequestParam @NotBlank String orderNo){
		TraceLogIdUtils.setTraceLogId(null);
		Response<OrderDetailResp> resp = new Response<OrderDetailResp>();
		try{
			log.info("获取订单详细信息接口开始执行");
			OrderDetailResp orderDetail = orderServiceImpl.getOrderDetail(token,orderNo);
			resp.setResult(orderDetail);
		}catch(DesignException ex){
			log.info("获取订单详细信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取订单详细信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/closeOrder",method=RequestMethod.POST)
//	public Response<Void> closeOrder(@RequestHeader(name="token",required=true)String token,@RequestParam String orderNo){
//		TraceLogIdUtils.setTraceLogId(null);
//		Response<Void> resp = new Response<Void>();
//		try{
//			log.info("取消订单接口开始执行");
//			orderServiceImpl.closeOrder(token,orderNo);
//			resp.setResult(null);
//		}catch(DesignException ex){
//			log.info("取消订单接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
//			resp.setRespCode(ex.getErrCode());
//			resp.setRespMessage(ex.getMessage());
//		}catch(Exception e){
//			log.info("取消订单接口执行异常,{}",Throwables.getStackTraceAsString(e));
//			resp.setRespCode(INTERNAL_ERROR.getErrCode());
//			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
//		}
//		return resp;
//	}
	
	@ResponseBody
	@RequestMapping(value="/sign",method=RequestMethod.POST)
	public Response<String> doSign(@RequestHeader(name="token",required=true)String token,SignReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("编辑订单币种接口开始...{}",req);
			ValidateUtils.validateEntiryThrows(req);
			String sign = orderServiceImpl.doSign(token,req);
			resp.setResult(sign);
		}catch(DesignException ex){
			log.info("获取订单列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取订单列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/payconfirm",method=RequestMethod.POST)
	public Response<Void> payConfirm(@RequestHeader(name="token",required=true)String token,@RequestParam String orderNo){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("订单支付确认接口开始执行");
			orderServiceImpl.payConfirm(token,orderNo);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("订单支付确认接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("订单支付确认接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

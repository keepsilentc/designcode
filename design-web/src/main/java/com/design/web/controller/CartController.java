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

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.common.utils.ValidateUtils;
import com.design.service.api.ICartService;
import com.design.service.api.dto.req.AddProduct2CartReq;
import com.design.service.api.dto.req.CheckoutInfoReq;
import com.design.service.api.dto.req.ModifyProductCartReq;
import com.design.service.api.dto.resp.CartOperationResp;
import com.design.service.api.dto.resp.CartProductInfoResp;
import com.design.service.api.dto.resp.CheckoutInfoResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/cart")
@Controller
public class CartController {
	private static Logger log = LoggerFactory.getLogger(CartController.class);
	@Resource
	private ICartService cartServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getProducts",method=RequestMethod.GET)
	public Response<List<CartProductInfoResp>> getProducts(@RequestHeader String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CartProductInfoResp>> resp = new Response<List<CartProductInfoResp>>();
		try{
			log.info("获取购物车内所有商品接口开始执行");
			List<CartProductInfoResp> result = cartServiceImpl.getProducts(token);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取购物车内所有商品接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取购物车内所有商品接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/addProduct",method=RequestMethod.POST)
	public Response<Void> addProduct(@RequestHeader String token,AddProduct2CartReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("加入商品到购物车接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			cartServiceImpl.addProduct(token,req);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("加入商品到购物车接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("加入商品到购物车接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/modifyProduct",method=RequestMethod.POST)
	public Response<CartOperationResp> modifyProduct(@RequestHeader String token,ModifyProductCartReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<CartOperationResp> resp = new Response<CartOperationResp>();
		try{
			log.info("修改购物车中商品接口开始执行");
			ValidateUtils.validateEntiryThrows(req);
			CartOperationResp numAndSum = cartServiceImpl.modifyProduct(token,req);
			resp.setResult(numAndSum);
		}catch(DesignException ex){
			log.info("修改购物车中商品接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("修改购物车中商品接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteProducts",method=RequestMethod.POST)
	public Response<CartOperationResp> deleteProducts(@RequestHeader String token,@RequestParam("ids") List<Long> ids){
		TraceLogIdUtils.setTraceLogId(null);
		Response<CartOperationResp> resp = new Response<CartOperationResp>();
		try{
			log.info("删除购物车中商品接口开始执行");
			CartOperationResp numAndSum = cartServiceImpl.deleteProducts(token,ids);
			resp.setResult(numAndSum);
		}catch(DesignException ex){
			log.info("删除购物车中商品接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("删除购物车中商品接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getCartCount",method=RequestMethod.GET)
	public Response<String> getCartCount(@RequestHeader String token){
		TraceLogIdUtils.setTraceLogId(null);
		Response<String> resp = new Response<String>();
		try{
			log.info("获取购物车中商品数量接口开始执行");
			Integer cartCount = cartServiceImpl.getCartCount(token);
			resp.setResult(String.valueOf(cartCount));
		}catch(DesignException ex){
			log.info("获取购物车中商品数量接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取购物车中商品数量接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getCheckoutInfo",method=RequestMethod.POST)
	public Response<CheckoutInfoResp> getCheckoutInfo(@RequestHeader String token,CheckoutInfoReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<CheckoutInfoResp> resp = new Response<CheckoutInfoResp>();
		try{
			log.info("检验选中的记录详情接口开始执行,{}",req);
			ValidateUtils.validateEntiryThrows(req);
			CheckoutInfoResp checkoutInfo = cartServiceImpl.getCheckoutInfo(token,req);
			resp.setResult(checkoutInfo);
		}catch(DesignException ex){
			log.info("检验选中的记录详情接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("检验选中的记录详情接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

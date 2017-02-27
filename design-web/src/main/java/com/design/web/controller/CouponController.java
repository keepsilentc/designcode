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
import com.design.service.api.ICouponService;
import com.design.service.api.dto.resp.CouponResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;

@RequestMapping("/coupon")
@Controller
public class CouponController {
	private static Logger log = LoggerFactory.getLogger(CouponController.class);
	
	@Resource
	private ICouponService couponServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getUserCouponList",method=RequestMethod.POST)
	public Response<List<CouponResp>> getUserCouponList(@RequestHeader String token,@RequestParam String validFlag){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CouponResp>> resp = new Response<List<CouponResp>>();
		try{
			log.info("查询用户优惠券接口开始执行");
			List<CouponResp> coupons = couponServiceImpl.getUserCouponList(token,validFlag);
			resp.setResult(coupons);
		}catch(DesignException ex){
			log.info("查询用户优惠券接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询用户优惠券接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCoupon",method=RequestMethod.POST)
	public Response<List<CouponResp>> searchCoupon(@RequestParam String couponName){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CouponResp>> resp = new Response<List<CouponResp>>();
		try{
			log.info("用户领取优惠券接口开始执行");
			List<CouponResp> coupons = couponServiceImpl.searchCoupon(couponName);
			resp.setResult(coupons);
		}catch(DesignException ex){
			log.info("用户领取优惠券接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("用户领取优惠券接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/receiveCoupon",method=RequestMethod.POST)
	public Response<Void> receiveCoupon(@RequestHeader String token,@RequestParam String couponNo){
		TraceLogIdUtils.setTraceLogId(null);
		Response<Void> resp = new Response<Void>();
		try{
			log.info("用户领取优惠券接口开始执行");
			couponServiceImpl.receiveCoupon(token,couponNo);
			resp.setResult(null);
		}catch(DesignException ex){
			log.info("用户领取优惠券接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("用户领取优惠券接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
}

package com.design.web.controller;

import static com.design.common.enums.DesignEx.INTERNAL_ERROR;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.design.common.assist.DesignException;
import com.design.common.utils.TraceLogIdUtils;
import com.design.service.api.IBrandService;
import com.design.service.api.ICategoryService;
import com.design.service.api.IProductService;
import com.design.service.api.dto.req.PreSTListReq;
import com.design.service.api.dto.req.ProductListReq;
import com.design.service.api.dto.resp.BrandResp;
import com.design.service.api.dto.resp.CategoryResp;
import com.design.service.api.dto.resp.PreSTListResp;
import com.design.service.api.dto.resp.ProductInfoResp;
import com.design.service.api.dto.resp.ProductListResp;
import com.design.web.dto.Response;
import com.google.common.base.Throwables;
@Controller
@RequestMapping("/product")
public class ProductController {
	private static Logger log = LoggerFactory.getLogger(ProductController.class);
	@Resource
	private IBrandService branServiceImpl;
	@Resource
	private ICategoryService categoryServiceImpl;
	@Resource
	private IProductService productServiceImpl;
	
	@ResponseBody
	@RequestMapping(value="/getBrands",method=RequestMethod.GET)
	public Response<List<BrandResp>> getBrands(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<BrandResp>> resp = new Response<List<BrandResp>>();
		try{
			log.info("查询所有品牌接口开始执行");
			List<BrandResp> result = branServiceImpl.getBrands();
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询所有品牌接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询所有品牌接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getCategorys",method=RequestMethod.GET)
	public Response<List<CategoryResp>> getCategorys(){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<CategoryResp>> resp = new Response<List<CategoryResp>>();
		try{
			log.info("查询所有类目接口开始执行");
			List<CategoryResp> result = categoryServiceImpl.getCategorys();
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("查询所有类目接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("查询所有类目接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getProductDetail",method=RequestMethod.POST)
	public Response<ProductInfoResp> getProductByProductNo(@RequestParam String productNo){
		TraceLogIdUtils.setTraceLogId(null);
		Response<ProductInfoResp> resp = new Response<ProductInfoResp>();
		try{
			log.info("根据商品号查询商品信息接口开始执行");
			ProductInfoResp result = productServiceImpl.getProductDetail(productNo);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("根据商品号查询商品信息接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("根据商品号查询商品信息接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getProducts",method=RequestMethod.POST)
	public Response<List<ProductListResp>> getProducts(ProductListReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<ProductListResp>> resp = new Response<List<ProductListResp>>();
		try{
			log.info("根据条件分页查询商品列表接口开始执行");
			List<ProductListResp> result = productServiceImpl.getProducts(req);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("根据条件分页查询商品列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("根据条件分页查询商品列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
	
	@ResponseBody
	@RequestMapping(value="/getPreSellThemes",method=RequestMethod.POST)
	public Response<List<PreSTListResp>> getPreSellThemes(PreSTListReq req){
		TraceLogIdUtils.setTraceLogId(null);
		Response<List<PreSTListResp>> resp = new Response<List<PreSTListResp>>();
		try{
			log.info("获取预售系列列表接口开始执行,{}",req);
			List<PreSTListResp> result = productServiceImpl.getPreSellThemes(req);
			resp.setResult(result);
		}catch(DesignException ex){
			log.info("获取预售系列列表接口执行异常,{}:{}",ex.getErrCode(),ex.getMessage());
			resp.setRespCode(ex.getErrCode());
			resp.setRespMessage(ex.getMessage());
		}catch(Exception e){
			log.info("获取预售系列列表接口执行异常,{}",Throwables.getStackTraceAsString(e));
			resp.setRespCode(INTERNAL_ERROR.getErrCode());
			resp.setRespMessage(INTERNAL_ERROR.getErrMsg());
		}
		return resp;
	}
}

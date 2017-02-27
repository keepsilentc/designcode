package com.design.service.impl.product;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.dao.entity.Product;
import com.design.dao.entity.ProductDetail;
import com.design.dao.entity.dto.PreSellThemeInfo;
import com.design.dao.entity.dto.ProductInfo;
import com.design.dao.entity.dto.ProductSizeColorInfo;
import com.design.dao.persist.ProductDetailMapper;
import com.design.dao.persist.ProductMapper;
import com.design.dao.persist.ProductSizeColorMapper;
import com.design.dao.persist.ThemeMapper;
import com.design.service.api.IProductService;
import com.design.service.api.dto.req.PreSTListReq;
import com.design.service.api.dto.req.ProductListReq;
import com.design.service.api.dto.resp.DesignProductResp;
import com.design.service.api.dto.resp.PreSTListResp;
import com.design.service.api.dto.resp.ProductInfoResp;
import com.design.service.api.dto.resp.ProductListResp;
import com.design.service.api.dto.resp.ProductSizeColorInfoResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class ProductServiceImpl implements IProductService {
	
	private static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Resource
	private ProductMapper productMapper;
	@Resource
	private ProductSizeColorMapper productSizeColorMapper;
	@Resource
	private ProductDetailMapper productDetailMapper;
	@Resource
	private ThemeMapper themeMapper;
	
	@Override
	public ProductInfoResp getProductDetail(String productNo) {
		log.info("获取产品细节信息,{}",productNo);
		//根据产品号获取产品信息
		final ProductInfo t_product = getEnableProductByProductNo(productNo);
		ProductInfoResp resp = DozerUtils.transfer(t_product, ProductInfoResp.class);
		//根据产品号获取产品详细图片
		List<ProductDetail> productDetailList = productDetailMapper.getProductDetailByProductNo(productNo);
		
		List<String> pictureList = Lists.newArrayList();
		for(ProductDetail productDetail:productDetailList){
			pictureList.add(String.valueOf(productDetail.getPicture()));
		}
		
		resp.setPictures(pictureList);
		//根据产品号获取产品颜色尺寸信息
		List<ProductSizeColorInfo> productColorSizes = getEnableProductColorSizeList(productNo);
		List<ProductSizeColorInfoResp> specifications = CollectionUtils.transfer(productColorSizes,ProductSizeColorInfoResp.class,new MyTransfer<ProductSizeColorInfo,ProductSizeColorInfoResp>(){
			@Override
			public void transfer(ProductSizeColorInfo u,
					ProductSizeColorInfoResp v) {
				if(u.getPicture()==null){
					v.setPicture(String.valueOf(t_product.getPicture()));
				}
				v.setPtscId(String.valueOf(u.getId()));
			}
		});
		resp.setSpecifications(specifications);
		return resp;
	}
	
	public List<ProductSizeColorInfo> getEnableProductColorSizeList(String productNo) {
		return productSizeColorMapper.getEnableProductColorSizeList(productNo);
	}
	
//	/**
//	 * 根据条件判断库存中是否存在相应数量的商品
//	 * @param req
//	 * @param param productNo,sizeId,colorId,productNum
//	 * @return
//	 */
//	public ProductSizeColorInfo getEnablePtscByCondition(Map<String, Object> param) {
//		ProductSizeColorInfo t_productSizeColor = productSizeColorMapper.getProductColorSizeByCondition(param);
//		Integer productNum = (Integer) param.get("productNum");
//		if(t_productSizeColor==null){
//			log.info("商品库中无此具体属性的商品...");
//			throw new DesignException(DesignEx.WAREHOUSENOTHISPRODUCT);
//		}else if(t_productSizeColor.getIsEnable().equals(Constant.UNENABLE)){
//			log.info("商品库中此商品未启用...{}",t_productSizeColor);
//			throw new DesignException(DesignEx.WAREHOUSE_PRODUCT_UNABLE);
//		}else if(t_productSizeColor.getInventory()<productNum){
//			log.info("商品库存不足...库存:{},需求:{}",t_productSizeColor.getInventory(),productNum);
//			throw new DesignException(DesignEx.SHORTINVENTORY);
//		}
//		return t_productSizeColor;
//	}
	
	public ProductSizeColorInfo getEnableNumPtscById(Long ptscId,Integer productNum) {
			ProductSizeColorInfo t_productSizeColor = productSizeColorMapper.getProductColorSizeById(ptscId);
			if(t_productSizeColor==null){
				log.info("商品库中无此具体属性的商品...");
				throw new DesignException(DesignEx.WAREHOUSENOTHISPRODUCT);
			}else if(Constant.UNENABLE.equals(t_productSizeColor.getIsEnable())){
				log.info("商品库中此商品未启用...{}",t_productSizeColor);
				throw new DesignException(DesignEx.WAREHOUSE_PRODUCT_UNABLE);
			}else if(t_productSizeColor.getInventory()<productNum){
				log.info("商品库存不足...库存:{},需求:{}",t_productSizeColor.getInventory(),productNum);
				throw new DesignException(DesignEx.SHORTINVENTORY);
			}
			return t_productSizeColor;
	}
	
	
	public Product getProductByProductNo(String productNo) {
		ProductInfo t_product = productMapper.getProductByProductNo(productNo);
		if(t_product==null){
			log.info("商品不存在");
			throw new DesignException(DesignEx.PRODUCTNOTFIND);
		}
		return t_product;
	}
	
	public ProductInfo getEnableProductByProductNo(String productNo) {
		ProductInfo t_product = productMapper.getProductByProductNo(productNo);
		if(t_product==null){
			log.info("商品不存在");
			throw new DesignException(DesignEx.PRODUCTNOTFIND);
		}else if(Constant.UNENABLE.equals(t_product.getIsEnable())){
			log.info("商品未启用");
			throw new DesignException(DesignEx.PRODUCTUNABLE);
		}
		return t_product;
	}

	@Override
	public List<ProductListResp> getProducts(ProductListReq req) {
		log.info("根据条件获取商品列表...{}",req);
		Map<String,Object> param = Maps.newHashMap();
		if(req.getDesignerId()!=null){
			param.put("designerId", req.getDesignerId());
		}
		if(req.getThemeId()!=null){
			param.put("themeId", req.getThemeId());
		}
		if(req.getCategoryId()!=null){
			param.put("categoryId",req.getCategoryId());
		}
		if(req.getIsNew()!=null){
			param.put("isNew", req.getIsNew());
		}
		if(req.getState()!=null){
			param.put("state", req.getState());
		}
		if(req.getSortRule()!=null){
			param.put("sortRule", req.getSortRule());
		}
		if(req.getPageSize()!=null){
			param.put("pageSize",req.getPageSize());
		}else{
			param.put("pageSize", Constant.PAGESIZE);
		}
		if(req.getPageIndex()!=null){
			param.put("pageIndex", (req.getPageIndex()-1)*(Integer)param.get("pageSize"));
		}else{
			param.put("pageIndex", 0);
		}
		
		List<ProductInfo> t_products = getProductListByParam(param);
		
		return DozerUtils.transferList(t_products, ProductListResp.class);
	}
	
	public List<ProductInfo> getProductListByParam(Map<String,Object> param){
		return productMapper.getProductList(param);
	}
	
	/**
	 * 更新商品库信息,如果未成功更新记录,抛异常
	 * @param operation,1：商品出库,商品回库
	 * @param productNum
	 * @param ptstId
	 */
	public void updateProductNumInStock(Integer operation,Integer productNum, Long ptstId) {
		if(productSizeColorMapper.updateProductNumInStock(operation,productNum,ptstId)!=1){
			if(Constant.PULL.equals(operation)){
				log.info("锁定商品失败,库存不足,productNum:{},id:{}",productNum,ptstId);
				throw new DesignException(DesignEx.SHORTINVENTORY);
			}else{
				log.info("商品回库失败,销售量不足,productNum:{},id:{}",productNum,ptstId);
				throw new DesignException(DesignEx.SHORTSALE);
			}
		}
	}


	public ProductSizeColorInfo getProductColorSizeById(Long id) {
		ProductSizeColorInfo t_productSizeColor = productSizeColorMapper.getProductColorSizeById(id);
		if(t_productSizeColor==null){
			log.info("商品库中无此具体属性的商品...");
			throw new DesignException(DesignEx.WAREHOUSENOTHISPRODUCT);
		}
		return t_productSizeColor;
	}

	public List<DesignProductResp> getAllProducts(Long id) {
		List<DesignProductResp> result =Lists.newArrayList();
		DesignProductResp tmp = null;
		List<Map<String,Object>> products = productMapper.getAllProducts(id);
		for(Map<String,Object> tmpMap :products){
			tmp = new DesignProductResp();
			tmp.setProductNo(String.valueOf(tmpMap.get("ProductNo")));
			tmp.setPicture(String.valueOf(tmpMap.get("Picture")));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public List<PreSTListResp> getPreSellThemes(PreSTListReq req) {
		Map<String,Object> param = Maps.newHashMap();
		if(req.getPageSize()!=null){
			param.put("pageSize",req.getPageSize());
		}else{
			param.put("pageSize", Constant.PAGESIZE);
		}
		if(req.getPageIndex()!=null){
			param.put("pageIndex", (req.getPageIndex()-1)*(Integer)param.get("pageSize"));
		}else{
			param.put("pageIndex", 0);
		}
		List<PreSellThemeInfo> preSellThemeInfoList = themeMapper.getPreSellThemes(param);
		List<PreSTListResp> preStlRespList = CollectionUtils.transfer(preSellThemeInfoList, PreSTListResp.class, new MyTransfer<PreSellThemeInfo, PreSTListResp>() {
			@Override
			public void transfer(PreSellThemeInfo u, PreSTListResp v) {
				v.setThemeId(String.valueOf(u.getId()));
			}
		});
		return preStlRespList;
	}

}

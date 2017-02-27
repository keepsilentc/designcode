package com.design.service.impl.product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.ProductState;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.Cart;
import com.design.dao.entity.Product;
import com.design.dao.entity.dto.CartInfo;
import com.design.dao.entity.dto.NumAndMoney;
import com.design.dao.entity.dto.ProductSizeColorInfo;
import com.design.dao.entity.dto.RetAddressInfo;
import com.design.dao.persist.CartMapper;
import com.design.service.api.ICartService;
import com.design.service.api.ICommonService;
import com.design.service.api.ICouponService;
import com.design.service.api.dto.req.AddProduct2CartReq;
import com.design.service.api.dto.req.CheckoutInfoReq;
import com.design.service.api.dto.req.ModifyProductCartReq;
import com.design.service.api.dto.resp.CartOperationResp;
import com.design.service.api.dto.resp.CartProductInfoResp;
import com.design.service.api.dto.resp.CheckoutInfoResp;
import com.design.service.api.dto.resp.ProductSizeColorInfoResp;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.design.service.impl.user.RetAddressServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class CartServiceImpl implements ICartService {
	private static Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Resource
	private ICouponService couponServiceImpl;
	@Resource
	private CartMapper cartMapper;
	@Resource
	private ProductServiceImpl productServiceImpl;
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private RetAddressServiceImpl retAddressServiceImpl;
	@Resource
	private ICommonService commonServiceImpl;
	
	@Override
	public List<CartProductInfoResp> getProducts(String token) {
		log.info("获取购物车内所有商品服务开始...");
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		List<CartInfo> t_products = cartMapper.getAllProductsByUserNo(userNo);
		
		List<CartProductInfoResp> resultList = CollectionUtils.transfer(t_products, CartProductInfoResp.class, new MyTransfer<CartInfo, CartProductInfoResp>() {
			@Override
			public void transfer(CartInfo u, CartProductInfoResp v) {
				v.setCartId(String.valueOf(u.getId()));
			}
		});
		
		for(CartProductInfoResp tmp:resultList){
			//根据产品号获取产品颜色尺寸信息
			List<ProductSizeColorInfo> productColorSizes = productServiceImpl.getEnableProductColorSizeList(tmp.getProductNo());
			
//			ProductSizeColorInfo t_ProductSizeColorInfo = productServiceImpl.getProductColorSizeById(Long.valueOf(tmp.getPtscId()));
//			if(Constant.UNENABLE.equals(t_ProductSizeColorInfo.getIsEnable())){
//				productColorSizes.add(t_ProductSizeColorInfo);
//			}
			
			List<ProductSizeColorInfoResp> specifications = CollectionUtils.transfer(productColorSizes,ProductSizeColorInfoResp.class,new MyTransfer<ProductSizeColorInfo,ProductSizeColorInfoResp>(){
				@Override
				public void transfer(ProductSizeColorInfo u,ProductSizeColorInfoResp v) {
					v.setPtscId(String.valueOf(u.getId()));
				}
			});
			
			tmp.setSpecifications(specifications);
		}
		
		return resultList;
	}

	
	public List<CartInfo> getEnableProductsByUserNo(String userNo) {
		List<CartInfo> t_products = cartMapper.getEnableProductsByUserNo(userNo);
		return t_products;
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void addProduct(String token, AddProduct2CartReq req) {
		log.info("加入商品到购物车服务开始...{}",req);
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		//获取仓库中的商品信息
		ProductSizeColorInfo t_productSizeColor = productServiceImpl.getEnableNumPtscById(req.getPtscId(),req.getProductNum());
		//
		Product t_product = productServiceImpl.getEnableProductByProductNo(t_productSizeColor.getProductNo());
		if(ProductState.PRE_SELL.getStateCode().equals(t_product.getState())){
			if(t_product.getPreSellEndTime()!=null&&DateUtil.isAfter(t_product.getPreSellEndTime())){
				throw new DesignException(DesignEx.PRESELLEXPIRE);
			}
		}
		//新增商品到购物车中
		addProducttoCart(req, userNo, t_productSizeColor);
	}

	private Cart addProducttoCart(AddProduct2CartReq req, String userNo,ProductSizeColorInfo t_productSizeColor) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("userNo", userNo);
		param.put("ptscId", t_productSizeColor.getId());
		Cart t_cart = cartMapper.getProductByCondition(param);
		if(t_cart==null){
			//购物车中无此商品
			t_cart = new Cart();
			t_cart.setUserNo(userNo);
			t_cart.setProductNo(t_productSizeColor.getProductNo());
			t_cart.setPtscId(t_productSizeColor.getId());
			t_cart.setProductNum(req.getProductNum());
			//添加商品到购物车
			int num = cartMapper.getCartCount(userNo);
			if(num>49){
				throw new DesignException(DesignEx.NUM_BEYOND_LIMIT);
			}
			cartMapper.insertProducttoCart(t_cart);
		}else{
			int productNum = req.getProductNum()+t_cart.getProductNum();
			//购物车中有此商品,修改数量
			if(t_productSizeColor.getInventory()<productNum){
				log.info("商品库存不足...库存:{},需求:{}",t_productSizeColor.getInventory(),productNum);
				throw new DesignException(DesignEx.SHORTINVENTORY);
			}
			t_cart.setProductNum(productNum);
			cartMapper.updateProductNuminCart(t_cart);
		}
		return t_cart;
	}


	@Override
	@Transactional
	public CartOperationResp modifyProduct(String token, ModifyProductCartReq req) {
		log.info("修改购物车中商品服务开始...{}",req);
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		//根据id获取购物车记录
		getCartById(req.getCartId());
		//编辑购物车中的商品
		modifyProductInCart(req,userNo);
		//获取购物车内商品数量和商品总价(前端校验用)
		NumAndMoney t_num_money = cartMapper.getCartSumNumAndMoney(userNo);
		CartOperationResp resp = new CartOperationResp();
		if(t_num_money!=null){
			resp.setNum(String.valueOf(t_num_money.getNum()));
			resp.setMoney(t_num_money.getMoney().toString());
		}
		return resp;
	}

	private Cart getCartById(Long cartId) {
		Cart cart = cartMapper.getCartById(cartId);
		if(cart==null){
			log.info("购物车中记录已被删除");
			throw new DesignException(DesignEx.CONCURRENCY_ERROR);
		}
		return cart;
	}

	private void modifyProductInCart(ModifyProductCartReq req,String userNo) {
		//获取仓库中具体属性的商品信息,如果没有或库存不足,直接返回
		ProductSizeColorInfo t_productSizeColor = productServiceImpl.getEnableNumPtscById(req.getPtscId(), req.getProductNum());
		Map<String,Object> param = Maps.newHashMap();
		param.put("userNo", userNo);
		param.put("ptscId", req.getPtscId());
		//查询购物车中具体属性的商品 
		Cart t_cart = cartMapper.getProductByCondition(param);
		if(t_cart==null){
			//购物车中无此属性商品
			log.debug("购物车中无此属性商品");
			t_cart = new Cart();
			t_cart.setUserNo(userNo);
			t_cart.setProductNo(t_productSizeColor.getProductNo());
			t_cart.setPtscId(t_productSizeColor.getId());
			t_cart.setProductNum(req.getProductNum());
			//添加此属性商品到购物车
			cartMapper.insertProducttoCart(t_cart);
			cartMapper.removeProductfromCart(req.getCartId(),userNo);
		}else{
			//购物车中有此属性商品
			//是相同属性商品(颜色和尺寸也相同),修改数量
			if(t_cart.getId().equals(req.getCartId())){
				log.debug("是相同属性商品(颜色和尺寸也相同),修改数量");
				t_cart.setProductNum(req.getProductNum());
				cartMapper.updateProductNuminCart(t_cart);
			}else{
				log.debug("是不同属性商品(颜色或尺寸不相同),把颜色和尺寸对应的该商品记录数量相加");
				//是不同属性商品(颜色或尺寸不相同),把颜色和尺寸对应的该商品记录数量相加
				t_cart.setProductNum(req.getProductNum()+t_cart.getProductNum());
				//获取仓库中具体属性的商品信息,如果没有或库存不足,直接返回
				productServiceImpl.getEnableNumPtscById(req.getPtscId(), t_cart.getProductNum());
				cartMapper.updateProductNuminCart(t_cart);
				//移除id对应的购物车商品记录
				cartMapper.removeProductfromCart(req.getCartId(),userNo);
			}
		}
	}

	@Override
	@Transactional
	public CartOperationResp deleteProducts(String token, List<Long> ids) {
		log.info("删除购物车中商品服务开始...{}",Arrays.toString(ids.toArray()));
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		deleteProductsInCart(userNo,ids);
		//获取购物车内商品数量和商品总价
		NumAndMoney t_num_mondy = cartMapper.getCartSumNumAndMoney(userNo);
		CartOperationResp resp = new CartOperationResp();
		if(t_num_mondy!=null){
			resp.setNum(String.valueOf(t_num_mondy.getNum()));
			resp.setMoney(t_num_mondy.getMoney().toString());
		}
		return resp;
	}

	public int deleteProductsInCart(String userNo,List<Long> ids) {
		return cartMapper.batchRmProductsInCart(ids,userNo);
	}


	@Override
	public Integer getCartCount(String token) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		return cartMapper.getCartCount(userNo);
	}


	@Override
	public CheckoutInfoResp getCheckoutInfo(String token, CheckoutInfoReq req) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		List<Long> cardIds = JSON.parseArray(req.getCartIds(),Long.class);
		//验证前端传送的订单购物车id在用户购物车中
		List<CartInfo> cartInfos = validateCartIds(cardIds,userNo);
		
		List<CartProductInfoResp> cartProductInfoList = CollectionUtils.transfer(cartInfos, CartProductInfoResp.class, new MyTransfer<CartInfo, CartProductInfoResp>() {
			@Override
			public void transfer(CartInfo u, CartProductInfoResp v) {
				
				v.setCartId(String.valueOf(u.getId()));
				ProductSizeColorInfo t_ProductSizeColorInfo = productServiceImpl.getProductColorSizeById(u.getPtscId());
				v.setSpecifications(Arrays.asList(DozerUtils.transfer(t_ProductSizeColorInfo, ProductSizeColorInfoResp.class,new MyTransfer<ProductSizeColorInfo, ProductSizeColorInfoResp>() {
					@Override
					public void transfer(ProductSizeColorInfo from,ProductSizeColorInfoResp to) {
						to.setPtscId(String.valueOf(from.getId()));
					}
				})));
			}
		});
		
		ChicunMoney checkoutMoney = new ChicunMoney();
		for(CartInfo tmp:cartInfos){
			if(ProductState.SPOT.getStateCode().equals(tmp.getProductState())){
				checkoutMoney = checkoutMoney.add(new ChicunMoney(tmp.getPrice()).multiply(tmp.getProductNum()));
			}else if(ProductState.PRE_SELL.getStateCode().equals(tmp.getProductState())){
				checkoutMoney = checkoutMoney.add(new ChicunMoney(tmp.getPreSalePrice()).multiply(tmp.getProductNum()));
			}
		}
		
		ChicunMoney freight = new ChicunMoney();
		
		if(req.getAddressId()!=null){
		
			RetAddressInfo t_addressInfo = retAddressServiceImpl.getAddressById(userNo,req.getAddressId());
			freight = freight.add(commonServiceImpl.getFreight(t_addressInfo.getCountryId(),checkoutMoney.getMoney().intValue()));
			
		}
		
		ChicunMoney discountMoney = new ChicunMoney();
		if(StringUtils.isNotEmpty(req.getCouponNo())){
			discountMoney = new ChicunMoney(couponServiceImpl.getDisountMoney(userNo, req.getCouponNo(), checkoutMoney.getMoney()));
		}
		
		CheckoutInfoResp result = new CheckoutInfoResp();
		result.setCheckoutMoney(checkoutMoney.toString());
		result.setDiscountMoney(discountMoney.toString());
		//应付款金额,订单商品总金额+运费-折扣金额
		ChicunMoney payAbleAmount = new ChicunMoney();
		payAbleAmount = checkoutMoney.add(freight).subtract(discountMoney);
		
		result.setPayAbleMoney(payAbleAmount.toString());
		result.setDiscountDescribe(null);
		result.setCheckoutInfoDetails(cartProductInfoList);
		result.setFreight(freight.toString());
		return result;
	}

	/**
	 * 验证购物车中是否包含这些记录,如果包括返回这些记录的详细信息,否则抛异常
	 * @param cartIds
	 * @param t_cartInfos
	 * @return
	 */
	public List<CartInfo> validateCartIds(List<Long> cartIds,String userNo) {
		
		if (!CollectionUtils.isNotEmptyDataNotNull(cartIds)){
			log.info("订单购物车id不能为空");
			throw new DesignException(DesignEx.ORDER_NOCARTIDS);
		}
		
		List<CartInfo> t_cartInfos = getEnableProductsByUserNo(userNo);
		boolean flag = false;
		List<CartInfo> order_carts = Lists.newArrayList();
		for(Long cartId:cartIds){
			flag = false;
			for(CartInfo tmp:t_cartInfos){
				if(cartId.equals(tmp.getId())){
					if(ProductState.PRE_SELL.getStateCode().equals(tmp.getProductState())){
						if(DateUtil.isAfter(tmp.getPreSellEndTime())){
							throw new DesignException(DesignEx.PRESELLEXPIRE);
						}else if(DateUtil.isBefore(tmp.getPreSellStartTime())){
							throw new DesignException(DesignEx.PRESELLNOTSTART);
						}
					}
					flag = true;
					order_carts.add(tmp);
					break;
				}
			}
			if(!flag){
				log.info("订单购物车id再此用户购物车列表中不存在,{}",cartId);
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			}
		}
		return order_carts;
	}

}

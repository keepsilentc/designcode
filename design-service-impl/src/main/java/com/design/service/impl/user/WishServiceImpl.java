package com.design.service.impl.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.MyTransfer;
import com.design.dao.entity.Wish;
import com.design.dao.entity.dto.ProductSizeColorInfo;
import com.design.dao.entity.dto.WishInfo;
import com.design.dao.persist.WishMapper;
import com.design.service.api.IWishService;
import com.design.service.api.dto.req.WishListReq;
import com.design.service.api.dto.resp.WishListResp;
import com.design.service.impl.product.ProductServiceImpl;
import com.google.common.collect.Maps;
@Service
public class WishServiceImpl implements IWishService {
	@Resource
	private WishMapper wishMapper;
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private ProductServiceImpl productServiceImpl;
	@Override
	public void addWish(String token,Long ptstcID) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		ProductSizeColorInfo t_product =  productServiceImpl.getProductColorSizeById(ptstcID);
		String productNo = t_product.getProductNo();
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo", userNo);
		params.put("productNo", productNo);
		params.put("ptscId", ptstcID);
		Wish t_wish = wishMapper.getWishByCondition(params);
		if(t_wish==null){
			t_wish = new Wish();
			t_wish.setUserNo(userNo);
			t_wish.setProductNo(productNo);
			t_wish.setPtscId(ptstcID);
			t_wish.setCreateTime(DateUtil.getCurrentDate());
			wishMapper.insert(t_wish);
		}
	}
	
	@Override
	public int deleteWish(String token, Long wishId) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token); 
		return wishMapper.delete(wishId,userNo);
	}

	@Override
	public List<WishListResp> getWishs(String token, WishListReq req) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo", userNo);
		if(req.getPageSize()!=null){
			params.put("pageSize",req.getPageSize());
		}else{
			params.put("pageSize", Constant.PAGESIZE);
		}
		if(req.getPageIndex()!=null){
			params.put("pageIndex", (req.getPageIndex()-1)*(Integer)params.get("pageSize"));
		}else{
			params.put("pageIndex", 0);
		}
		List<WishInfo> wishInfoList = wishMapper.getWishInfoList(params);
		List<WishListResp> wishRespList = CollectionUtils.transfer(wishInfoList, WishListResp.class, new MyTransfer<WishInfo, WishListResp>() {

			@Override
			public void transfer(WishInfo u, WishListResp v) {
				v.setWishId(String.valueOf(u.getId()));
			}
		});
		return wishRespList;
	}

	@Override
	public void wishToCart(String token, Long wishId) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Wish t_wish = getWishById(wishId);
		if(userNo.equals(t_wish.getUserNo())){
			
		}else{
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
	}
	
	public Wish getWishById(Long wishId){
		Wish t_wish = wishMapper.getWishById(wishId);
		if(t_wish==null){
			throw new DesignException(DesignEx.WISH_NOT_EXIST);
		}
		return t_wish;
	}
	
}

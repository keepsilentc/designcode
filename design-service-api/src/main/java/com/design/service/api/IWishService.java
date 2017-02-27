package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.WishListReq;
import com.design.service.api.dto.resp.WishListResp;

public interface IWishService {

	void addWish(String token,Long ptstcID);

	int deleteWish(String token, Long wishId);

	List<WishListResp> getWishs(String token, WishListReq req);

	void wishToCart(String token, Long wishId);

}

package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.AddProduct2CartReq;
import com.design.service.api.dto.req.CheckoutInfoReq;
import com.design.service.api.dto.req.ModifyProductCartReq;
import com.design.service.api.dto.resp.CartProductInfoResp;
import com.design.service.api.dto.resp.CartOperationResp;
import com.design.service.api.dto.resp.CheckoutInfoResp;

public interface ICartService {

	List<CartProductInfoResp> getProducts(String token);

	void addProduct(String token, AddProduct2CartReq req);

	CartOperationResp modifyProduct(String token, ModifyProductCartReq req);

	CartOperationResp deleteProducts(String token, List<Long> ids);

	Integer getCartCount(String token);

	CheckoutInfoResp getCheckoutInfo(String token, CheckoutInfoReq req);

}

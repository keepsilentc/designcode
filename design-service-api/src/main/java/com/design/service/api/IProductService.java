package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.PreSTListReq;
import com.design.service.api.dto.req.ProductListReq;
import com.design.service.api.dto.resp.PreSTListResp;
import com.design.service.api.dto.resp.ProductInfoResp;
import com.design.service.api.dto.resp.ProductListResp;

public interface IProductService {

	List<ProductListResp> getProducts(ProductListReq req);
	
	ProductInfoResp getProductDetail(String productNo);

	List<PreSTListResp> getPreSellThemes(PreSTListReq req);

	

}

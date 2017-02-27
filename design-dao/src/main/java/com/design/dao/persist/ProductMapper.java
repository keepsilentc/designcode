package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.dto.ProductInfo;


public interface ProductMapper {

	List<ProductInfo> getProductList(Map<String, Object> param);

	ProductInfo getProductByProductNo(String productNo);

	List<Map<String, Object>> getAllProducts(Long designerId);
	
}

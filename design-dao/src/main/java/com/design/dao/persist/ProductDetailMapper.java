package com.design.dao.persist;

import java.util.List;

import com.design.dao.entity.ProductDetail;


public interface ProductDetailMapper {
	
	List<ProductDetail> getProductDetailByProductNo(String productNo);

	void update(ProductDetail productDetail);

	void insert(ProductDetail tmp);
	
}

package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.Cart;
import com.design.dao.entity.dto.CartInfo;
import com.design.dao.entity.dto.NumAndMoney;

public interface CartMapper {

	Cart getProductByCondition(Map<String, Object> param);
	
	Cart getCartById(Long id);
	
	void insertProducttoCart(Cart t_cart);

	int updateProductNuminCart(Cart t_cart);

	NumAndMoney getCartSumNumAndMoney(String userNo);

	List<CartInfo> getAllProductsByUserNo(String userNo);
	
	List<CartInfo> getEnableProductsByUserNo(String userNo);

	int removeProductfromCart(@Param("id")Long cartId,@Param("userNo")String userNo);

	int batchRmProductsInCart(@Param("ids")List<Long> ids,@Param("userNo")String userNo);

	Integer getCartCount(String userNo);

	

	
	
}

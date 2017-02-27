package com.design.dao.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.dto.ProductSizeColorInfo;

public interface ProductSizeColorMapper {
	
	List<ProductSizeColorInfo> getEnableProductColorSizeList(String productNo);

	ProductSizeColorInfo getProductColorSizeById(Long id);
	
	int updateProductNumInStock(@Param(value="operation") Integer operation,@Param(value = "productNum")Integer productNum,@Param(value = "id")Long id);

}

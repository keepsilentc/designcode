package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.Wish;
import com.design.dao.entity.dto.WishInfo;


public interface WishMapper {

	Wish getWishByCondition(Map<String, Object> params);

	void insert(Wish t_wish);

	int delete(@Param(value = "id")Long wishId, @Param(value = "userNo")String userNo);

	List<WishInfo> getWishInfoList(Map<String, Object> params);

	Wish getWishById(Long wishId);


}

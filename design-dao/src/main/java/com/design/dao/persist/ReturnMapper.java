package com.design.dao.persist;
import java.util.List;
import java.util.Map;

import com.design.dao.entity.Returns;


public interface ReturnMapper {
	
	void insert(Returns returns);
	
	void update(Returns returns);

	List<Returns> getReturnsList(Map<String, Object> param);

	Returns getReturnByReturnNo(String refundNo);
	
}

package com.design.dao.persist;

import com.design.dao.entity.OrderLog;


public interface OrderLogMapper {

	void insertOrderLog(OrderLog log);

	int updateOrderLog(OrderLog orderLog);


}

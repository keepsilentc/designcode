package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.design.dao.entity.Order;


public interface OrderMapper {

	void insertOrder(Order t_order);

	Order getOrderByOrderNo(@Param(value = "orderNo")String orderNo,@Param(value = "userNo")String userNo);

	List<Order> getCanCloseOrders(int autoclosetime);

	int closeOrder(Order tmp);

	int updateOrderAtnofity(Order order);
	
	List<Order> getOrderList(Map<String, Object> param);

	List<Order> getRefundOrderList(Map<String, Object> param);

	int updateConfirm(Order order);

	int updateOrderCurrency(Order t_order);

	List<Order> getSplitOrders(String orderNo);

}

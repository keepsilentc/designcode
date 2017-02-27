package com.design.dao.persist;

import java.util.List;

import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.dto.OrderDetailInfo;


public interface OrderDetailMapper {

	void insertOrderDetail(OrderDetail detail);

	List<OrderDetail> getOrderDetailList(String orderNO);

	int updateOrderDetailAtnotify(OrderDetail detail);

	OrderDetail getOrderDetailById(Long id);

	int updateOrderDetailSteteAtRefund(OrderDetail t_orderDetail);

	Integer getOrderProductNum(String orderNo);

	List<OrderDetailInfo> getOrderDetailInfos(List<String> orderNos);


}

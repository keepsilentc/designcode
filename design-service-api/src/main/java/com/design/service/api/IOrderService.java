package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.CreateOrderReq;
import com.design.service.api.dto.req.OrderListReq;
import com.design.service.api.dto.req.SignReq;
import com.design.service.api.dto.resp.OrderDetailResp;
import com.design.service.api.dto.resp.OrderListResp;

public interface IOrderService {

	String createOrder(String token,CreateOrderReq req);

	List<OrderListResp> getOrders(String token, OrderListReq req);

	OrderDetailResp getOrderDetail(String token, String orderNO);

	void closeOrder(String token, String orderNo);

	List<OrderListResp> getRefundOrders(String token);

	String doSign(String token, SignReq req);
	
	void payConfirm(String token, String orderNo);

}

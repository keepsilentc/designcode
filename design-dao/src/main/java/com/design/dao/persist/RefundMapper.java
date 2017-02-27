package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.Refund;


public interface RefundMapper {

	void insert(Refund refund);

	void update(Refund refund);

	List<Refund> getRefundList(Map<String, Object> param);

	Integer getRefundProductNum(String orderNo);

	Refund getRefundByRefundNo(String refundNo);

}

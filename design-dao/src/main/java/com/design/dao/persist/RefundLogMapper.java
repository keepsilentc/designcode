package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.RefundLog;


public interface RefundLogMapper {

	void insert(RefundLog refundLog);

	void update(RefundLog refundLog);

	List<RefundLog> getRefundLogList(Map<String, Object> params);

}

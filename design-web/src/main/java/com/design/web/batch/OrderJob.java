package com.design.web.batch;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.design.common.assist.Constant;
import com.design.common.enums.OrderOperateType;
import com.design.common.utils.DateUtil;
import com.design.common.utils.StringUtils;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderLog;
import com.design.service.impl.order.OrderServiceImpl;
import com.google.common.base.Throwables;

public class OrderJob {
	
	private static Logger log = LoggerFactory.getLogger(OrderJob.class);
	
	@Resource
	private OrderServiceImpl orderServiceImpl;
	
	public void autoCloseOrder(){
		TraceLogIdUtils.setTraceLogId(null);
		log.info("自动关闭未付款订单任务开始....{}",DateUtil.getCurrent(DateUtil.allPattern));
		List<Order> orders = null;
		try {
			orders = orderServiceImpl.getCanCloseOrders(Constant.AUTOCLOSETIME);
			log.info("获取可以关闭订单数量:{}",orders.size());
		} catch (Exception e) {
			log.info(Throwables.getStackTraceAsString(e));
			return;
		}
		for(Order order:orders){
			OrderLog orderLog = null;
			try{
				//订单信息入订单日志表
				orderLog = initOrderLog(order);
				orderServiceImpl.closeOrderAndDeal(order);
				orderLog.setOperateType(OrderOperateType.CLOSE_SUCCESS.getTypeCode());
			}catch(Exception e){
				String errMsg = Throwables.getStackTraceAsString(e);
				orderLog.setOperateType(OrderOperateType.CLOSE_FAIL.getTypeCode());
				if(StringUtils.isNotEmpty(errMsg)){
					orderLog.setFailReason(errMsg.substring(0, Math.max(200, errMsg.length())));
				}
				log.info(Throwables.getStackTraceAsString(e));
			}finally{
				orderServiceImpl.updateOrderLog(orderLog);
			}
			
		}
		log.info("任务执行完成...");
		
	}

	private OrderLog initOrderLog(Order order) {
		OrderLog orderLog = new OrderLog();
		orderLog.setUserNo(order.getUserNo());
		orderLog.setCurrencyId(order.getCurrencyId());
		orderLog.setOrderMoney(order.getOrderMoney());
		orderLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		orderLog.setCreateTime(DateUtil.getCurrentDate());
		orderLog.setOperateType(OrderOperateType.CLOSE_INIT.getTypeCode());
		orderServiceImpl.insertOrderLog(orderLog);
		return orderLog;
	}
	
}

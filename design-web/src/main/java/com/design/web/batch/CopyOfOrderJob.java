package com.design.web.batch;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.design.common.assist.Constant;
import com.design.common.utils.DateUtil;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.service.impl.order.OrderServiceImpl;
import com.google.common.base.Throwables;

public class CopyOfOrderJob {
	private static Logger log = LoggerFactory.getLogger(CopyOfOrderJob.class);
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private DataSourceTransactionManager txManager;
	
	public void autoCloseOrder(){
		
		TraceLogIdUtils.setTraceLogId(null);
		log.info("自动关闭未付款订单任务开始....{}",DateUtil.getCurrent(DateUtil.allPattern));
		try {
			List<Order> orders = orderServiceImpl.getCanCloseOrders(Constant.AUTOCLOSETIME);
			log.info("获取可以关闭订单数量:{}",orders.size());
			for(@SuppressWarnings("unused") Order order:orders){
				//log入库
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();  
		        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);  
		        TransactionStatus status =  txManager.getTransaction(def);
				try{
					txManager.commit(status);
				}catch(Exception e){
					txManager.rollback(status);
				}finally{
					
				}
			}
		} catch (Exception e) {
			log.info(Throwables.getStackTraceAsString(e));
		}
	}
	
	void rsa(){
//		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//		model.setSubject("尺寸商品");
//		model.setOutTradeNo(orderNo);
//		model.setTimeoutExpress("120m");
//		model.setTotalAmount(order.getOrderMoney().toString());
//		model.setProductCode("QUICK_MSECURITY_PAY");
//		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//		request.setApiVersion("1.0");
//		request.setNotifyUrl("http://localhost:8080/design/pay/alipay/notify.do");
//		request.setBizModel(model);
//		try {
//			params = AlipaySignature.getSortedMap(alipayUtil.getRequestHolderWithSign(request));
//		} catch (AlipayApiException e) {
//			throw new DesignException(DesignEx.ALIPAY_SIGN_FAIL);
//		}
	}
}

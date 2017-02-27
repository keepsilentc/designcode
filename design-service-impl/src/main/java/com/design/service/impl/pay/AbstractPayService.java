package com.design.service.impl.pay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderState;
import com.design.common.enums.OrderType;
import com.design.common.enums.PayLoadType;
import com.design.common.enums.ProductState;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.DateUtil;
import com.design.common.utils.MoneyUtil;
import com.design.common.utils.NoGenerator;
import com.design.common.utils.StringUtils;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.Trade;
import com.design.dao.entity.TradeLog;
import com.design.dao.entity.dto.OrderDetailInfo;
import com.design.dao.persist.TradeLogMapper;
import com.design.dao.persist.TradeMapper;
import com.design.service.api.ICouponService;
import com.design.service.api.IPayLoadService;
import com.design.service.api.IPayLoadStrategy;
import com.design.service.api.IThirdTradeService;
import com.design.service.api.dto.pay.PayNotifyResp;
import com.design.service.api.dto.payload.PayLoad;
import com.design.service.impl.order.CurrencyServiceImpl;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.padload.MailUtil;
import com.design.service.impl.template.FreeMarkUtil;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public abstract class AbstractPayService implements IThirdTradeService {
	
	private static Logger log = LoggerFactory.getLogger(AbstractPayService.class);
	
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private CurrencyServiceImpl currencyServiceImpl;
	@Resource
	private TradeMapper tradeMapper;
	@Resource
	private TradeLogMapper tradeLogMapper;
	@Resource
	private IPayLoadService payLoadServiceImpl;
	@Resource
	private ICouponService couponServiceImpl;
	@Resource
	private FreeMarkUtil freeMarkUtil;
	@Resource
	private MailUtil mailUtil;
	
	public String getUserNoByToken(String token){
		return defaultUserServiceImpl.getUserNoByToken(token);
	}
	
	public Order getOrderByOrderNo(String orderNo,String userNo){
		return orderServiceImpl.getOrderByOrderNo(orderNo,userNo);
	}
	
	public void payConfirm(String token, String orderNo) {
		String userNo = getUserNoByToken(token);
		Order order = getOrderByOrderNo(orderNo,userNo);
		boolean flag = false;
		if(OrderState.OBLIGATION.getStateCode().equals(order.getOrderState())){
			//待付款订单
			flag = true;
			order.setOrderState(OrderState.CONFIRM.getStateCode());
		}else{
			if(OrderType.PRE_SELL.getTypeCode().equals(order.getOrderType())
					&&OrderState.PRE_PAY_SUCCESS.getStateCode().equals(order.getOrderState())){
				//预售订单,二次付款
				flag = true;
				order.setOrderState(OrderState.CONFIRM.getStateCode());
			}
		}
		if(flag){
			orderServiceImpl.payConfirm(order);
		}
	}
	
	public abstract CommonNotify parseParamToBean(Map<String, String> params);
	
	public abstract boolean signCheck(Map<String, String> params);
	
	public abstract void checkNotify(Order order,CommonNotify notifyBean);
	
	@Override
	public PayNotifyResp payNotify(Map<String, String> params) {
		PayNotifyResp result = new PayNotifyResp();
		final CommonNotify notifyBean = parseParamToBean(params);
		log.info("回掉通知实体,{}",notifyBean);
		//记录交易日志
		TradeLog tradeLog = insertTradeLog(params,notifyBean);
		boolean signVerified = true;
		try {
			signVerified = signCheck(params);
			log.info("验签结果:{}",signVerified);
			if(signVerified){
				tradeLog.setIsSignVerified(Constant.SUCCESS_STATE);
				//防止重复通知
				Trade trade = tradeMapper.getTradeByOrderNo(notifyBean.getOut_trade_no());
				if(trade!=null){
					log.info("交易不可多次入库");
					tradeLog.setFailReason("交易不可多次入库");
					result.setReturn_code(Constant.SUCCESS);
					return result;
				}
				final Order order = orderServiceImpl.getOrderByOrderNo(notifyBean.getOut_trade_no(), null);
				Order master_order = (Order) order.clone();
				
				log.info("验证回掉信息");
				checkNotify(order,notifyBean);
				
				//信息存入交易表
				log.info("信息存入交易表");
				insertTrade(order,notifyBean);
				
				//处理尺寸内部逻辑
				try {
					log.info("处理尺寸内部逻辑");
					((AbstractPayService)AopContext.currentProxy()).dealNotifiedOrder(notifyBean,order);
					tradeLog.setIsSuccess(Constant.SUCCESS_STATE);
				} catch (Exception e) {
					log.info(Throwables.getStackTraceAsString(e));
					tradeLog.setIsSuccess(Constant.FAIL_STATE);
				}
				try{
					//发送邮件
					{
						Map<String,Object> param = Maps.newHashMap();
						param.put("address", master_order.getAddress());
						param.put("payId", master_order.getPayId());
						param.put("payAbleMoney",master_order.getOrderMoney());
						if(StringUtils.isNotEmpty(master_order.getCurrencyId())){
							String sign = currencyServiceImpl.getCurrency(master_order.getCurrencyId()).getSign();
							param.put("outSign", sign);
						}
						
						List<String> orderNos = Lists.newArrayList(); 
						if(!OrderType.PRE_SELL.getTypeCode().equals(master_order.getOrderType())){
							//非预售
							orderNos.add(master_order.getOrderNo());
						}
						if(!OrderType.SALE.getTypeCode().equals(master_order.getOrderType())){
							//非现货
							List<Order> splitOrders = orderServiceImpl.getSplitOrders(master_order.getOrderNo());
							for(Order tmp:splitOrders){
								orderNos.add(tmp.getOrderNo());
							}
						}
						int iMax = orderNos.size()-1;

				        StringBuilder b = new StringBuilder();
				        for (int i = 0; ; i++) {
				            b.append(orderNos.get(i));
				            if (i == iMax)
				                break;
				            b.append(", ");
				        }
						param.put("orderNos", b.toString());
						JSONArray jsonArray = new JSONArray();
						List<OrderDetailInfo> orderDetailInfos = orderServiceImpl.getOrderDetailInfos(orderNos);
						String inSign = null;
						for(OrderDetailInfo orderDetailInfo:orderDetailInfos){
							orderDetailInfo.setPrice(new ChicunMoney(orderDetailInfo.getPtsumMoney()).divide(orderDetailInfo.getPtBuyNum()).getMoney());
							jsonArray.add(orderDetailInfo);
							if(inSign==null){
								if(StringUtils.isNotEmpty(orderDetailInfo.getCurrencyId())){
									String sign = currencyServiceImpl.getCurrency(orderDetailInfo.getCurrencyId()).getSign();
									param.put("inSign", sign);
								}
							}
						}
						
						param.put("orderDetails", orderDetailInfos);
						
						JSONObject addressJson = JSONObject.parseObject(master_order.getAddress());
						log.info("参数：{}",param);
						String content = freeMarkUtil.getContent(param, "orderreceipt.ftl");
						log.info("邮件内容为:{}",content);
						mailUtil.transport("CHICUN尺寸—订单收据", content, addressJson.get("email").toString());
						
					}
					
					//推送
					payLoadServiceImpl.exec(new IPayLoadStrategy() {
						
						@Override
						public PayLoad getPayLoad() {
							PayLoad payLoad = new PayLoad();
							payLoad.setPayLoadType(PayLoadType.DEFAULT.getTypeCode());
							payLoad.setTitle("支付成功");
							StringBuilder builder = new StringBuilder();
							builder.append("您的订单已完成支付，金额");
							builder.append(notifyBean.getTotal_amount());
							builder.append("，订单已在处理中");
							List<String> deviceTokens = Lists.newArrayList();
							deviceTokens.add(defaultUserServiceImpl.getUserByUserNo(order.getUserNo()).getDeviceToken());
							payLoad.setDeviceTokens(deviceTokens);
							return payLoad;
						}
					});
				}catch(Exception e){
					log.info("发送邮件或推送消息异常,{}",Throwables.getStackTraceAsString(e));
				}
				
				
				result.setReturn_code(Constant.SUCCESS);
				return result;
			}else{
				tradeLog.setIsSignVerified(Constant.FAIL_STATE);
				result.setReturn_code(Constant.FAIL);
				result.setReturn_msg("验证签名错误");
				return result;
			}
			
		} catch (Exception e) {
			log.info(Throwables.getStackTraceAsString(e));
			//验签异常
			tradeLog.setFailReason(e.getMessage().substring(0, Math.min(e.getMessage().length(), 200)));
			throw new DesignException(DesignEx.ALIPAY_ERROR);
		}finally{
			tradeLog.setUpdateBy("alipayNotify");
			tradeLog.setUpdateTime(DateUtil.getCurrentDate());
			tradeLogMapper.update(tradeLog);
		}
	}
	
	@Transactional
	public void dealNotifiedOrder(CommonNotify notifyBean,Order order) {
		order.setPayId(notifyBean.getPay_type());
		//获取订单详细商品
		List<OrderDetail> t_orderdetails = orderServiceImpl.getOrderDetailList(order.getOrderNo());
		if(OrderType.SALE.getTypeCode().equals(order.getOrderType())){
			log.info("现货订单");
			OrderDetail tmp = null;
			//现货商品
			for(int i=0,len = t_orderdetails.size();i<len;i++){
				tmp = t_orderdetails.get(i);
				orderServiceImpl.updateOrderDetailAtnotify(tmp);
			}
			order.setOrderState(OrderState.PAY_SUCCESS.getStateCode());
		}else if(OrderType.PRE_SELL.getTypeCode().equals(order.getOrderType())){
			log.info("预售订单");
			//预售二次付款通知
			if(StringUtils.isNotEmpty(order.getPreOrderNo())){
				order.setOrderMoney(MoneyUtil.getTotalMoney(order.getOrderMoney()).getMoney());
				order.setDiscountMoney(MoneyUtil.getTotalMoney(order.getDiscountMoney()).getMoney());
				order.setOrderState(OrderState.PAY_SUCCESS.getStateCode());
			}else{
				OrderDetail tmp = null;
				//预售商品
				for(int i=0,len = t_orderdetails.size();i<len;i++){
					tmp = t_orderdetails.get(i);
						//拆分订单
					Order splitOrder = splitPreSaleProduct(notifyBean.getTrade_no(),order,tmp);
					tmp.setOrderNo(splitOrder.getOrderNo());
					tmp.setPreOrderNo(order.getOrderNo());
					orderServiceImpl.updateOrderDetailAtnotify(tmp);
				}
				order.setIsShow(0);
			}
			
		}else if(OrderType.MIX_SELL.getTypeCode().equals(order.getOrderType())){
			log.info("混合订单");
			OrderDetail tmp = null;
			//现货订单总额
			ChicunMoney allPtSumMoney = null;
			//处理预售与混合
			for(int i=0,len = t_orderdetails.size();i<len;i++){
				tmp = t_orderdetails.get(i);
				//混合订单,把预售商品拆分成单独的订单
				log.info("混合订单处理逻辑开始...{}",order.getOrderNo());
				if(ProductState.PRE_SELL.getStateCode().equals(tmp.getProductState())){
					//拆分预售商品作为新单
					log.info("处理预售商品...{}",tmp);
					Order splitOrder = splitPreSaleProduct(notifyBean.getTrade_no(),order,tmp);
					tmp.setOrderNo(splitOrder.getOrderNo());
					tmp.setPreOrderNo(order.getOrderNo());
				}else{
					if(allPtSumMoney==null){
						allPtSumMoney = new ChicunMoney(tmp.getPtsumMoney());
					}else{
						allPtSumMoney = allPtSumMoney.add(tmp.getPtsumMoney());
					}
				}
				orderServiceImpl.updateOrderDetailAtnotify(tmp);
			}
			log.info("混合现货总额：{}",allPtSumMoney);
			BigDecimal disCountMoney = couponServiceImpl.getDisountMoney(order.getUserNo(), order.getCouponNo(), allPtSumMoney.getMoney());
			log.info("混合现货打折金额：{}",disCountMoney);
			//应付款金额,订单商品总金额+运费-折扣金额
			ChicunMoney payAbleAmount = new ChicunMoney();
			payAbleAmount = allPtSumMoney.add(order.getFreight()).subtract(disCountMoney);
			order.setOrderMoney(payAbleAmount.getMoney());
			order.setDiscountMoney(disCountMoney);
			order.setOrderState(OrderState.PAY_SUCCESS.getStateCode());
			order.setOrderType(ProductState.SPOT.getStateCode());
		}
		order.setUpdateBy("alipayNotify");
		order.setUpdateTime(DateUtil.getCurrentDate());
		log.info("更新订单表状态");
		orderServiceImpl.updateOrderAtnofity(order);
	}
	
	private Order splitPreSaleProduct(String tradeNo,Order order,OrderDetail orderDetail) {
		Order splitOrder = new Order();
		splitOrder.setAddress(order.getAddress());
		splitOrder.setRemark(order.getRemark());
		splitOrder.setCreateTime(DateUtil.getCurrentDate());
		splitOrder.setCurrencyId(order.getCurrencyId());
		
		BigDecimal halfPtSumMoney = MoneyUtil.getHalfMoney(orderDetail.getPtsumMoney()).getMoney();
		splitOrder.setDiscountMoney(new ChicunMoney().getMoney());
		splitOrder.setFreight(new ChicunMoney().getMoney());
		//折扣金额
		if(StringUtils.isNotEmpty(order.getCouponNo())){
			splitOrder.setCouponNo(order.getCouponNo());
			splitOrder.setDiscountMoney(couponServiceImpl.getDisountMoney(order.getUserNo(), order.getCouponNo(), halfPtSumMoney));
		}
		//应付金额,订单金额+运费-折扣金额
		ChicunMoney payAbleAmount = new ChicunMoney();
		payAbleAmount = payAbleAmount.add(halfPtSumMoney).subtract(splitOrder.getDiscountMoney());
		splitOrder.setOrderMoney(payAbleAmount.getMoney());
		
		splitOrder.setOrderType(ProductState.PRE_SELL.getStateCode());
		splitOrder.setOrderState(OrderState.PRE_PAY_SUCCESS.getStateCode());
		splitOrder.setTradeNo(tradeNo);
		splitOrder.setUserNo(order.getUserNo());
		splitOrder.setPayId(order.getPayId());
		splitOrder.setModifyCount(0);
		splitOrder.setOrderNo(NoGenerator.generateOrderNo());
		splitOrder.setPreOrderNo(order.getOrderNo());
		splitOrder.setIsShow(1);
		orderServiceImpl.insertOrderAtnotify(splitOrder);
		return splitOrder;
	}

	
	private TradeLog insertTradeLog(Map<String, String> params,CommonNotify notifyBean) {
		TradeLog tradeLog = new TradeLog();
		tradeLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		tradeLog.setOrderNo(notifyBean.getOut_trade_no());
		tradeLog.setTradeNo(notifyBean.getTrade_no());
		tradeLog.setPayTypeId(notifyBean.getPay_type());
		tradeLog.setNotify(JSON.toJSONString(params));
		tradeLog.setCreateTime(DateUtil.getCurrentDate());
		tradeLogMapper.insert(tradeLog);
		return tradeLog;
	}
	
	private void insertTrade(Order order,CommonNotify notifyBean) {
		Trade trade = new Trade();
		trade.setOrderNo(notifyBean.getOut_trade_no());
		trade.setUserNo(order.getUserNo());
		trade.setTradeNo(notifyBean.getTrade_no());
		trade.setCurrencyId(notifyBean.getCurrency_id());
		trade.setPayTypeId(notifyBean.getPay_type());
		trade.setPayMoney(ChicunMoney.parseBigDecimal(notifyBean.getTotal_amount()));
		trade.setPayTime(notifyBean.getPay_time());
		trade.setOrderMoney(ChicunMoney.parseBigDecimal(notifyBean.getTotal_amount()));
		trade.setCreateTime(DateUtil.getCurrentDate());
		tradeMapper.insert(trade);
	}

	@Override
	public Trade getTradeByOrderNo(String orderNo) {
		return tradeMapper.getTradeByOrderNo(orderNo);
	}
	
}

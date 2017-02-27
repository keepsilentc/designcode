package com.design.service.impl.order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderDetailState;
import com.design.common.enums.OrderOperateType;
import com.design.common.enums.OrderState;
import com.design.common.enums.OrderType;
import com.design.common.enums.ProductState;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MoneyUtil;
import com.design.common.utils.NoGenerator;
import com.design.common.utils.StringUtils;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.OrderLog;
import com.design.dao.entity.Product;
import com.design.dao.entity.dto.CartInfo;
import com.design.dao.entity.dto.OrderDetailInfo;
import com.design.dao.entity.dto.ProductSizeColorInfo;
import com.design.dao.entity.dto.RetAddressInfo;
import com.design.dao.persist.OrderDetailMapper;
import com.design.dao.persist.OrderLogMapper;
import com.design.dao.persist.OrderMapper;
import com.design.service.api.ICouponService;
import com.design.service.api.IOrderService;
import com.design.service.api.IUserService;
import com.design.service.api.dto.req.CreateOrderReq;
import com.design.service.api.dto.req.OrderListReq;
import com.design.service.api.dto.req.SignReq;
import com.design.service.api.dto.resp.OrderDetailResp;
import com.design.service.api.dto.resp.OrderListResp;
import com.design.service.api.dto.resp.OrderPtscDetail;
import com.design.service.api.dto.resp.TradeQueryResp;
import com.design.service.impl.common.CommonServiceImpl;
import com.design.service.impl.pay.PayFacade;
import com.design.service.impl.pay.alipay.AlipayUtil;
import com.design.service.impl.product.CartServiceImpl;
import com.design.service.impl.product.ProductServiceImpl;
import com.design.service.impl.user.RetAddressServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class OrderServiceImpl implements IOrderService {
	@Resource
	private IUserService defaultUserServiceImpl;
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private CurrencyServiceImpl currencyServiceImpl;
	@Resource
	private RetAddressServiceImpl retAddressServiceImpl;
	@Resource
	private CartServiceImpl cartServiceImpl;
	@Resource
	private ProductServiceImpl productServiceImpl;
	@Resource
	private ICouponService couponServiceImpl;
	@Resource
	private OrderDetailMapper orderDetailMapper;
	@Resource
	private OrderMapper orderMapper;
	@Resource
	private OrderLogMapper orderLogMapper;
	@Resource
	private AlipayUtil alipayUtil;
	@Resource
	private PayFacade payFacadeImpl;
	
	private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Override
	public String createOrder(String token,CreateOrderReq req) {
		log.info("创建订单服务开始...,{}",req);
		//获取客户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		List<Long> cartIds = JSON.parseArray(req.getCartIdsStr(),Long.class);
		req.setCartIds(cartIds);
		//验证前端传送的订单购物车id
		List<CartInfo> order_cartInfos = cartServiceImpl.validateCartIds(cartIds,userNo);
		
		//订单信息入订单日志表
		OrderLog orderLog = insertOrderLog(req,Constant.CNY,userNo);
		log.debug("订单信息入订单日志表,{}",orderLog);
		try{
			//创建订单逻辑操作(service内部调用其他方法,事务不会加强,需调用proxy)
			String orderNo = ((OrderServiceImpl)AopContext.currentProxy()).createOrderAndDelProductsInCart(req, userNo, order_cartInfos, orderLog);
			return orderNo;
		}catch(Exception e){
			orderLog.setOperateType(OrderOperateType.CREATE_FAIL.getTypeCode());
			orderLog.setFailReason(e.getMessage());
			throw e;
		}finally{
			log.debug("更新订单日志表,{}",orderLog);
			orderLog.setUpdateTime(DateUtil.getCurrentDate());
			updateOrderLog(orderLog);
		}
	}

	public void updateOrderLog(OrderLog orderLog) {
		orderLogMapper.updateOrderLog(orderLog);
	}
	
	/**
	 * 创建订单总逻辑接口
	 * @param req
	 * @param userNo
	 * @param order_cartInfos
	 * @param orderLog
	 * @return
	 */
	@Transactional
	public String createOrderAndDelProductsInCart(CreateOrderReq req,String userNo, List<CartInfo> order_cartInfos, OrderLog orderLog) {
		//生成订单实体
		Order t_order = generateOrder(userNo,req);
		//订单详细入详细表******(重要操作)
		List<OrderDetail> orderDetailList = dealOrderDetail(req, order_cartInfos,t_order);
		ChicunMoney allPtSumMoney = new ChicunMoney();
		for(OrderDetail orderDetail:orderDetailList){
			//计算产品需要支付价格
			if(ProductState.PRE_SELL.getStateCode().equals(orderDetail.getProductState())){
				allPtSumMoney = allPtSumMoney.add(MoneyUtil.getHalfMoney(orderDetail.getPtsumMoney()));
			}else{
				allPtSumMoney = allPtSumMoney.add(orderDetail.getPtsumMoney());
			}
		}
		
		//折扣金额
		if(StringUtils.isNotEmpty(req.getCouponNo())){
			t_order.setCouponNo(req.getCouponNo());
			t_order.setDiscountMoney(couponServiceImpl.disountMoney(userNo, req.getCouponNo(), allPtSumMoney.getMoney()));
			couponServiceImpl.lockCoupon(userNo,req.getCouponNo());
		}
		
		
		//运费
		if(req.getAddressId()!=null){
			ChicunMoney freight = new ChicunMoney();
			RetAddressInfo t_addressInfo = retAddressServiceImpl.getAddressById(userNo,req.getAddressId());
			freight = freight.add(commonServiceImpl.getFreight(t_addressInfo.getCountryId(),t_order.getOrderMoney().intValue()));
			t_order.setFreight(freight.getMoney());
		}
		
		//应付款金额,订单商品总金额+运费-折扣金额
		ChicunMoney payAbleAmount = new ChicunMoney();
		payAbleAmount = payAbleAmount.add(allPtSumMoney).add(t_order.getFreight()).subtract(t_order.getDiscountMoney());
		t_order.setOrderMoney(payAbleAmount.getMoney());
		
		if(!payAbleAmount.equals(req.getOrderMoney())){
			log.debug("订单总金额不相等,payAbleAmount:{},orderMoney:{}",payAbleAmount,req.getOrderMoney());
			throw new DesignException(DesignEx.ORDER_MONEY_NOTSAME);
		}
		
		
		//入订单表******(重要操作)
		log.debug("入订单表,{}",t_order);
		orderMapper.insertOrder(t_order);
		//删除购物车中想应的数据******(重要操作)
		log.debug("删除购物车中相应的数据,{}",Arrays.toString(req.getCartIds().toArray()));
		int delnum = cartServiceImpl.deleteProductsInCart(userNo,req.getCartIds());
		log.debug("删除数量,{}",delnum);
		if(delnum!=req.getCartIds().size()){
			throw new DesignException(DesignEx.CONCURRENCY_ERROR);
		}
		orderLog.setOrderType(t_order.getOrderType());
		orderLog.setOrderNo(t_order.getOrderNo());
		orderLog.setOperateType(OrderOperateType.CREATE_SUCCESS.getTypeCode());
		return t_order.getOrderNo();
	}
	
	public void lockProductsInStock(Long ptstId,Integer productNum) {
		//根据条件判断库存中是否存在相应数量的商品
		productServiceImpl.getEnableNumPtscById(ptstId, productNum);
		updateProductNumInStock(Constant.PULL,productNum,ptstId);
	}
	
	/**
	 * 更新商品库信息,如果未成功更新记录,抛异常
	 * @param productNum
	 * @param id
	 */
	public void updateProductNumInStock(Integer operation,Integer productNum,Long ptstId){
		productServiceImpl.updateProductNumInStock(operation,productNum,ptstId);
	}
	/**
	 * 创建订单时,处理订单中的详细商品
	 * @param req
	 * @param order_cartInfos
	 * @param orderNo
	 * @return
	 */
	private List<OrderDetail> dealOrderDetail(CreateOrderReq req,List<CartInfo> order_cartInfos, Order order) {
		Integer orderType = null;
		List<OrderDetail> orderDetailList = Lists.newArrayList();
		for(CartInfo tmp:order_cartInfos){
			//锁定商品,库存加相应数量,销量增加相应数量 ******(重要操作)
			log.info("锁定商品开始...");
			lockProductsInStock(tmp.getPtscId(),tmp.getProductNum());
			log.info("锁定商品完成...");
			Product t_product = productServiceImpl.getEnableProductByProductNo(tmp.getProductNo());
			//生成订单详细表实体,入库
			OrderDetail detail = insertOrderDetail(order.getOrderNo(), tmp, t_product);
			if(orderType==null){
				orderType = t_product.getState();
			}else if(!orderType.equals(t_product.getState())){
				orderType = OrderType.MIX_SELL.getTypeCode();
			}
			orderDetailList.add(detail);
		}
		order.setOrderType(orderType);
		return orderDetailList;
	}
	
	private OrderLog insertOrderLog(CreateOrderReq req,String currencyId,String userNo) {
		OrderLog orderLog = new OrderLog();
		orderLog.setUserNo(userNo);
		orderLog.setCurrencyId(currencyId);
		orderLog.setOrderMoney(req.getOrderMoney());
		orderLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		orderLog.setCreateTime(DateUtil.getCurrentDate());
		orderLog.setOperateType(OrderOperateType.CREATE_INIT.getTypeCode());
		orderLogMapper.insertOrderLog(orderLog);
		return orderLog;
	}
	
	private OrderDetail insertOrderDetail(String orderNo,CartInfo tmp, Product t_product) {
		OrderDetail detail = new OrderDetail();
		detail.setOrderNo(orderNo);
		detail.setProductNo(tmp.getProductNo());
		detail.setPtstId(tmp.getPtscId());
		detail.setPtBuyNum(tmp.getProductNum());
		detail.setCurrencyId(Constant.CNY);
		detail.setModifyCount(Constant.ZERO);
		//产品总金额,数量*单价
		detail.setPtsumMoney(new ChicunMoney(t_product.getPrice()).multiply(tmp.getProductNum()).getMoney());
		detail.setProductState(t_product.getState());
		detail.setOrderDetailState(OrderDetailState.INIT.getStateCode());
		detail.setCreateTime(DateUtil.getCurrentDate());
		log.debug("入订单详细表,{}",detail);
		orderDetailMapper.insertOrderDetail(detail);
		return detail;
	}
	
	private Order generateOrder(String userNo,CreateOrderReq req) {
		Order order = new Order();
		RetAddressInfo t_addressInfo = retAddressServiceImpl.getAddressById(userNo,req.getAddressId());
		JSONObject json = new JSONObject();
		json.put("mobileNo", t_addressInfo.getMobileNo());
		json.put("postCode", t_addressInfo.getPostCode());
		json.put("recipients", t_addressInfo.getFirstName()+t_addressInfo.getLastName());
		if(StringUtils.isNotEmpty(t_addressInfo.getEmail())){
			json.put("email", t_addressInfo.getEmail());
		}
		if(Constant.CHINA.equals(t_addressInfo.getCountryId())){
			json.put("country", "中国");
			json.put("province", t_addressInfo.getProvince());
			json.put("city", t_addressInfo.getCity());
			json.put("region", t_addressInfo.getRegion());
		}else{
			json.put("country", t_addressInfo.getCountry());
			json.put("town", t_addressInfo.getTown());
			json.put("district", t_addressInfo.getDistrict());
		}
		json.put("addressInfo", t_addressInfo.getAddress());
		order.setAddress(json.toJSONString());
		order.setCurrencyId(Constant.CNY);
		order.setOrderMoney(new ChicunMoney(req.getOrderMoney()).getMoney());
		order.setDiscountMoney(new ChicunMoney().getMoney());
		order.setFreight(new ChicunMoney().getMoney());
		//设置订单为待付款
		order.setOrderState(OrderState.OBLIGATION.getStateCode());
		order.setRemark(req.getRemark());
		order.setUserNo(userNo);
		order.setCreateTime(DateUtil.getCurrentDate());
		order.setOrderNo(NoGenerator.generateOrderNo());
		order.setModifyCount(0);
		order.setIsShow(1);
		return order;
	}
	

	@Override
	public OrderDetailResp getOrderDetail(String token, String orderNo) {
		log.info("获取订单详细信息服务开始...,{}",orderNo);
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Order t_order = getOrderByOrderNo(orderNo,userNo);
		List<OrderDetail> t_orderDetails =  getOrderDetailList(orderNo);
		OrderDetailResp resp = generateResp(t_order,t_orderDetails);
		return resp;
	}
	
	public List<OrderDetail> getOrderDetailList(String orderNo){
		return orderDetailMapper.getOrderDetailList(orderNo);
	}
	
	private OrderDetailResp generateResp(Order t_order,List<OrderDetail> t_orderDetails) {
		OrderDetailResp resp = DozerUtils.transfer(t_order, OrderDetailResp.class);
		resp.setCurrencySign(currencyServiceImpl.getCurrency(t_order.getCurrencyId()).getSign());
		if(StringUtils.isNotEmpty(t_order.getPayId())){
			resp.setPayName(commonServiceImpl.getPayType(t_order.getPayId()).getName());
		}
		
		ChicunMoney totalMoney = new ChicunMoney();
		List<OrderPtscDetail> ptscDetails = Lists.newArrayList();
		OrderPtscDetail tmp_ptscDetail = null;
		for(OrderDetail tmp:t_orderDetails){
			ProductSizeColorInfo t_ProductSizeColor =  productServiceImpl.getProductColorSizeById(tmp.getPtstId());
			tmp_ptscDetail = new OrderPtscDetail();
			tmp_ptscDetail.setOrderDetailId(String.valueOf(tmp.getId()));
			tmp_ptscDetail.setPtstId(String.valueOf(t_ProductSizeColor.getId()));
			tmp_ptscDetail.setProductName(t_ProductSizeColor.getProductName());
			tmp_ptscDetail.setColorName(t_ProductSizeColor.getColorName());
			tmp_ptscDetail.setSizeName(t_ProductSizeColor.getSizeName());
			tmp_ptscDetail.setPicture(t_ProductSizeColor.getPicture()==null?null:t_ProductSizeColor.getPicture().toString());
			tmp_ptscDetail.setColorPicture(t_ProductSizeColor.getColorPicture()==null?null:t_ProductSizeColor.getColorPicture().toString());
			tmp_ptscDetail.setProductNo(tmp.getProductNo());
			tmp_ptscDetail.setPtBuyNum(String.valueOf(tmp.getPtBuyNum()));
			tmp_ptscDetail.setOrderDetailState(String.valueOf(tmp.getOrderDetailState()));
			tmp_ptscDetail.setPtsumMoney(tmp.getPtsumMoney().toString());
			tmp_ptscDetail.setPrice(new ChicunMoney(tmp.getPtsumMoney()).divide(tmp.getPtBuyNum()).toString());
			ptscDetails.add(tmp_ptscDetail);
			totalMoney = totalMoney.add(tmp.getPtsumMoney());
		}
		resp.setTotalMoney(totalMoney.toString());
		resp.setPtscDetails(ptscDetails);
		return resp;
	}
	
	/**
	 * 根据订单号获取订单(如果订单不存在,抛异常)
	 * @param orderNO
	 * @return
	 */
	public Order getOrderByOrderNo(String orderNo,String userNo){
		Order t_order = orderMapper.getOrderByOrderNo(orderNo,userNo);
		if(t_order==null){
			log.info("订单不存在,{}",orderNo);
			throw new DesignException(DesignEx.ORDER_NOT_EXISTS);
		}
		return t_order;
	}

	public List<Order> getCanCloseOrders(int autoclosetime) {
		return orderMapper.getCanCloseOrders(autoclosetime);
	}

	
	public int updateOrderAtnofity(Order order) {
		return orderMapper.updateOrderAtnofity(order);
	}

	public void insertOrderAtnotify(Order preorder) {
		orderMapper.insertOrder(preorder);
	}

	public int updateOrderDetailAtnotify(OrderDetail tmp) {
		return orderDetailMapper.updateOrderDetailAtnotify(tmp);
	}

	public OrderDetail getOrderDetailById(Long orderdetailId) {
		OrderDetail t_orderDetail = orderDetailMapper.getOrderDetailById(orderdetailId);
		if(t_orderDetail==null){
			log.info("订单详细不存在,id:{}",orderdetailId);
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		return t_orderDetail;
	}

	public int updateOrderDetailSteteAtRefund(OrderDetail t_orderDetail) {
		return orderDetailMapper.updateOrderDetailSteteAtRefund(t_orderDetail);
	}

	@Override
	public void closeOrder(String token, String orderNo) {
		log.info("用户关闭订单服务开始...,{}",orderNo);
		//获取客户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Order order = this.getOrderByOrderNo(orderNo, userNo);
		//处理关闭订单逻辑
		//订单信息入订单日志表
		OrderLog orderLog = insertOrderLog(order);
		try{
			//关闭订单逻辑操作(service内部调用其他方法,事务不会加强,需调用proxy)
			((OrderServiceImpl)AopContext.currentProxy()).closeOrderAndDeal(order);
			orderLog.setOperateType(OrderOperateType.CLOSE_SUCCESS.getTypeCode());
		}catch(Exception e){
			orderLog.setOperateType(OrderOperateType.CLOSE_FAIL.getTypeCode());
			orderLog.setFailReason(e.getMessage());
			throw e;
		}finally{
			log.debug("更新订单日志表,{}",orderLog);
			orderLog.setUpdateTime(DateUtil.getCurrentDate());
			updateOrderLog(orderLog);
		}
	}
	
	public void insertOrderLog(OrderLog orderLog) {
		orderLogMapper.insertOrderLog(orderLog);
	}
	
	private OrderLog insertOrderLog(Order order) {
		OrderLog orderLog = new OrderLog();
		orderLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		orderLog.setOrderNo(order.getOrderNo());
		orderLog.setUserNo(order.getUserNo());
		orderLog.setCurrencyId(order.getCurrencyId());
		orderLog.setOrderType(order.getOrderType());
		orderLog.setOrderMoney(order.getOrderMoney());
		orderLog.setOperateType(OrderOperateType.CLOSE_INIT.getTypeCode());
		orderLog.setFailReason(Constant.USERCLOSE);
		orderLog.setCreateTime(DateUtil.getCurrentDate());
		log.debug("订单信息入订单日志表,{}",orderLog);
		orderLogMapper.insertOrderLog(orderLog);
		return orderLog;
	}
	
	/**
	 * 关闭订单
	 * @param order 订单
	 */
	public void closeOrder(Order order){
		//处理关闭订单逻辑
		//订单信息入订单日志表
		OrderLog orderLog = insertOrderLog(order);
		try{
			//关闭订单逻辑操作(service内部调用其他方法,事务不会加强,需调用proxy)
			((OrderServiceImpl)AopContext.currentProxy()).closeOrderAndDeal(order);
			orderLog.setOperateType(OrderOperateType.CLOSE_SUCCESS.getTypeCode());
		}catch(Exception e){
			orderLog.setOperateType(OrderOperateType.CLOSE_FAIL.getTypeCode());
			orderLog.setFailReason(e.getMessage());
			throw e;
		}finally{
			log.debug("更新订单日志表,{}",orderLog);
			orderLog.setUpdateTime(DateUtil.getCurrentDate());
			updateOrderLog(orderLog);
		}
	}
	
	@Transactional
	public void closeOrderAndDeal(Order order) {
		log.info("开始关闭订单：{}",order);
		if(OrderState.OBLIGATION.getStateCode().equals(order.getOrderState())){
			//待付款订单
			//无付款方式信息
			if(StringUtils.isEmpty(order.getPayId())){
				//关闭订单,商品回归仓库
				closeOrderAndPush(order);
				return;
			}
			String outTradeNo = order.getOrderNo();
			TradeQueryResp queryResp =  payFacadeImpl.tradeQuery(outTradeNo,null,order.getPayId());
			order.setTradeNo(queryResp.getTradeNo());
			if(queryResp.isCanClose()){
				//关闭订单,商品回归仓库
				closeOrderAndPush(order);
			}
		}else{
			order.setOrderState(OrderState.FAIL.getStateCode());
			order.setUpdateTime(DateUtil.getCurrentDate());
			if(orderMapper.closeOrder(order)!=1){
				log.info("关闭订单失败");
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			}
		}
		
	}

	private void closeOrderAndPush(Order order) {
		order.setOrderState(OrderState.FAIL.getStateCode());
		order.setUpdateTime(DateUtil.getCurrentDate());
		if(orderMapper.closeOrder(order)==1){
			//订单详细数量回归商品库
			List<OrderDetail> t_orderDetails = getOrderDetailList(order.getOrderNo());
			for(OrderDetail orderDetail:t_orderDetails){
				updateProductNumInStock(Constant.PUSH,orderDetail.getPtBuyNum(),orderDetail.getPtstId());
			}
		}else{
			log.info("关闭订单失败");
			throw new DesignException(DesignEx.CONCURRENCY_ERROR);
		}
		
	}
	
	
	@Override
	public List<OrderListResp> getOrders(String token, OrderListReq req) {
		log.info("获取订单列表服务开始...,{}",req);
		//获取客户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		log.info("根据条件获取商品列表...{}",req);
		
		Map<String,Object> param = Maps.newHashMap();
		param.put("userNo", userNo);
		if(req.getTimeRange()!=null){
			param.put("timeRange", req.getTimeRange());
		}
		if(req.getPageSize()!=null){
			param.put("pageSize",req.getPageSize());
		}else{
			param.put("pageSize", Constant.PAGESIZE);
		}
		if(req.getPageIndex()!=null){
			param.put("pageIndex", (req.getPageIndex()-1)*(Integer)param.get("pageSize"));
		}else{
			param.put("pageIndex", 0);
		}
		List<Integer> orderStates = Lists.newArrayList();
		Integer orderStateConvert = req.getOrderStateConvert();
		if(4==orderStateConvert){
			//售后
			List<Order> t_orders = orderMapper.getRefundOrderList(param);
			return DozerUtils.transferList(t_orders, OrderListResp.class);
		}
		else if(2==orderStateConvert){
			//预售
			param.put("orderType", OrderType.PRE_SELL.getTypeCode().toString());
		}else if(1==orderStateConvert){
			orderStates.add(OrderState.OBLIGATION.getStateCode());
			param.put("orderState", orderStates);
		}else if(3==orderStateConvert){
			orderStates.add(OrderState.PAY_SUCCESS.getStateCode());
			orderStates.add(OrderState.PRE_PAY_SUCCESS.getStateCode());
			param.put("orderState", orderStates);
		}else if(0!=orderStateConvert){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
		List<Order> t_orders = orderMapper.getOrderList(param);
		return DozerUtils.transferList(t_orders, OrderListResp.class);
	}
	
	@Override
	public List<OrderListResp> getRefundOrders(String token) {
		log.info("获取退款订单列表服务开始...");
		//获取客户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Map<String,Object> param = Maps.newHashMap();
		param.put("userNo", userNo);
		List<Order> t_orders = orderMapper.getRefundOrderList(param);
		return DozerUtils.transferList(t_orders, OrderListResp.class);
	}

	public Integer getOrderProductNum(String orderNo) {
		return orderDetailMapper.getOrderProductNum(orderNo);
	}

	public int payConfirm(Order order) {
		return orderMapper.updateConfirm(order);
	}

	@Override
	@Transactional
	public String doSign(String token, SignReq req) {
		//获取客户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		String orderNo = req.getOrderNo();
		String currencyId = req.getCurrencyId();
		Order t_order = getOrderByOrderNo(orderNo, userNo);
		t_order.setPayId(req.getPayId());
		if("ALIPAY".equals(req.getPayId())||"WECHAT".equals(req.getPayId())){
			if(!"CNY".equals(currencyId)){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
		}else{
			if(!"EUR".equals(currencyId)
					&&!"GBP".equals(currencyId)&&!"USD".equals(currencyId)){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
		}
		
		String payId = req.getPayId();
		//获取签名信息
		String sign =  payFacadeImpl.sign(userNo,orderNo,payId,currencyId);
		return sign;
	}

	@Override
	public void payConfirm(String token, String orderNo) {
		payFacadeImpl.payConfirm(token,orderNo);
	}

	public List<Order> getSplitOrders(String orderNo) {
		return orderMapper.getSplitOrders(orderNo);
	}

	public List<OrderDetailInfo> getOrderDetailInfos(List<String> orderNos) {
		// TODO Auto-generated method stub
		return orderDetailMapper.getOrderDetailInfos(orderNos);
	}

	public void updateOrderCurrency(Order t_order) {
		//更新付款方式,订单金额,订单币种
		orderMapper.updateOrderCurrency(t_order);
		
	}

}

package com.design.service.impl.refund;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderDetailState;
import com.design.common.enums.OrderState;
import com.design.common.enums.OrderType;
import com.design.common.enums.PayLoadType;
import com.design.common.enums.ProductState;
import com.design.common.enums.RecordType;
import com.design.common.enums.RefundState;
import com.design.common.enums.RefundType;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MoneyUtil;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.NoGenerator;
import com.design.common.utils.StringUtils;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.Product;
import com.design.dao.entity.Refund;
import com.design.dao.entity.RefundLog;
import com.design.dao.entity.Returns;
import com.design.dao.entity.Trade;
import com.design.dao.persist.RefundLogMapper;
import com.design.dao.persist.RefundMapper;
import com.design.service.api.IAttachmentService;
import com.design.service.api.IPayLoadService;
import com.design.service.api.IPayLoadStrategy;
import com.design.service.api.IRefundService;
import com.design.service.api.IThirdTradeService;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.TradeMoneyRemainDto;
import com.design.service.api.dto.payload.PayLoad;
import com.design.service.api.dto.req.RefundApplyReq;
import com.design.service.api.dto.req.RefundReq;
import com.design.service.api.dto.resp.RefundFollowResp;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.product.ProductServiceImpl;
import com.design.service.impl.returns.ReturnServiceImpl;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RefundServiceImpl implements IRefundService{
	
	private static Logger log = LoggerFactory.getLogger(RefundServiceImpl.class);
	
	private Map<String,IRefundDetailService> refundDetailServices;
	
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private ProductServiceImpl productServiceImpl;
	@Resource 
	private ReturnServiceImpl returnServiceImpl;
	@Resource
	private IThirdTradeService commonPayServiceImpl;
	@Resource
	private IAttachmentService attachmentServiceImpl;
	@Resource
	private RefundMapper refundMapper;
	@Resource
	private RefundLogMapper refundLogMapper;
	@Resource
	private IPayLoadService payLoadServiceImpl;
	
	@Override
	@Transactional
	public void refundApply(String token,RefundApplyReq req,List<AttachmentDto> attachmentDtos) {
		log.info("申请退款接口开始执行...");
		defaultUserServiceImpl.getUserByToken(token);
		
		//查询订单详细信息
		OrderDetail orderDetail = orderServiceImpl.getOrderDetailById(req.getOrderDetailId());
		//生成逻辑dto
		RefundDto refundDto = generateRefundDtoApply(req.getRefundNum(),orderDetail,NoGenerator.generateRefundNo());
		refundDto.setRefundType(RefundType.REFUND);
		refundDto.setReason(req.getReason());
		if(CollectionUtils.isNotEmpty(attachmentDtos)){
			List<Long> attachmentIds = attachmentServiceImpl.batchInsert(attachmentDtos);
			StringBuilder builder = new StringBuilder();
			for(Long tmp:attachmentIds){
				if(builder.length()>0){
					builder.append(",");
				}
				builder.append(tmp);
			}
			refundDto.setPictures(builder.toString());
		}
		
		//初始化退款日志
		RefundLog refundLog = insertRefundLog(refundDto,RecordType.APPLY);
		refundDto.setRefundLog(refundLog);
		/*if(StringUtils.isNotEmpty(req.getRefundNo())){
			Refund refund = getRefundByRefundNo(req.getRefundNo());
			if(refund!=null){
				if(RefundState.REFUND_APPROVE_FAIL.getStateCode().equals(refund.getRefundState())){
					refund.setRefundState(RefundState.REFUND_APPROVEING.getStateCode());
					refundMapper.update(refund);
					refundLog.setRefundNo(refund.getRefundNo());
					refundLog.setUpdateBy("refundApply");
					refundLog.setUpdateTime(DateUtil.getCurrentDate());
					refundLogMapper.update(refundLog);
					return;
				}else{
					log.info("无退款请求号对应的退款");
					throw new DesignException(DesignEx.CANNOT_REFUND);
				}
			}
		}*/
		
		//验证是否可以退款
		log.info("验证是否可以退款");
		validateRefundApply(refundDto);
		
		//初始化退款表
		log.info("初始化退款表");
		insertRefund(refundDto,RefundState.REFUND_APPROVEING);
		
		orderDetail.setOrderDetailState(OrderDetailState.REFUNDING.getStateCode());
		orderServiceImpl.updateOrderDetailSteteAtRefund(orderDetail);
		
	}
	
	

	private Refund getRefundByRefundNo(String refundNo) {
		Refund refund = refundMapper.getRefundByRefundNo(refundNo);
		if(refund==null){
			throw new DesignException(DesignEx.REFUND_NOT_EXISTS);
		}
		return refund;
	}
	
	
	private Refund insertRefund(RefundDto refundDto,RefundState refundState) {
		Refund refund = new Refund();
		refund.setUserNo(refundDto.getOrder().getUserNo());
		refund.setOrderNo(refundDto.getOrder().getOrderNo());
		refund.setOrderDetailId(refundDto.getOrderDetail().getId());
		refund.setRefundState(refundState.getStateCode());
		refund.setRefundNo(refundDto.getRefundNo());
		refund.setRefundType(refundDto.getRefundType().getTypeCode());
		refund.setRefundNum(refundDto.getRefundNum());
		refund.setRefundMoney(refundDto.getRefundMoney());
		refund.setPayTypeId(refundDto.getOrder().getPayId());
		refund.setCurrencyId(refundDto.getOrder().getCurrencyId());
		refund.setCreateTime(DateUtil.getCurrentDate());
		refundMapper.insert(refund);
		return refund;
	}
	
	@Override
	public TradeMoneyRemainDto getTradeMoneyReamin(String orderNo,String refundNo){
		Trade t_trade = commonPayServiceImpl.getTradeByOrderNo(orderNo);
		if(t_trade==null){
			log.info("交易表中不存在,{}",orderNo);
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		Map<String,Object> param = Maps.newHashMap();
		param.put("orderNo", orderNo);
		List<Refund> refundList = getRefundList(param);
		ChicunMoney refundedMoney = new ChicunMoney();
		for(Refund refund_in:refundList){
			if(StringUtils.isNotEmpty(refundNo)){
				if(!refund_in.getRefundNo().equals(refundNo)){
					refundedMoney = refundedMoney.add(refund_in.getRefundMoney());
				}
			}else{
				refundedMoney = refundedMoney.add(refund_in.getRefundMoney());
			}
		}
		ChicunMoney remainMoney = new ChicunMoney(t_trade.getPayMoney());
		remainMoney = remainMoney.subtract(refundedMoney);
		TradeMoneyRemainDto tradeMRDto = new TradeMoneyRemainDto();
		tradeMRDto.setOrderNo(orderNo);
		tradeMRDto.setTrade(t_trade);
		tradeMRDto.setRefundList(refundList);
		tradeMRDto.setRemainMoney(remainMoney.getMoney());
		return tradeMRDto;
	}
	
	public void validateRefundApply(RefundDto refundDto) {
		Order order = refundDto.getOrder();
		OrderDetail orderDetail = refundDto.getOrderDetail();
		Integer refundNum = refundDto.getRefundNum();
		Integer orderState = order.getOrderState();
		Integer orderDetailState = orderDetail.getOrderDetailState();
		
		if(!OrderState.PAY_SUCCESS.getStateCode().equals(orderState)&&!OrderState.PRE_PAY_SUCCESS.getStateCode().equals(orderState)){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}else{
			if(OrderDetailState.REFUNDING.equals(orderDetailState)||OrderDetailState.CLOSE.equals(orderDetailState)){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}
		}
		//验证数量
		Map<String,Object> param = Maps.newHashMap();
		param.put("orderNo", order.getOrderNo());
		param.put("orderDetailId",orderDetail.getId());
		List<Refund> refundedList = getRefundList(param);
		Integer remainNum = orderDetail.getPtBuyNum();
		for(Refund tmp:refundedList){
			remainNum = remainNum - tmp.getRefundNum();
			if(!RefundState.REFUND_SUCCESS.getStateCode().equals(tmp.getRefundState())){
				log.info("存在未退款成功退款,{}",tmp);
				throw new DesignException(DesignEx.NOT_REFUNDED);
			}
		}
		if(refundNum > remainNum){
			log.info("退款个数错误,退：{},剩余：{}",refundNum,remainNum);
			throw new DesignException(DesignEx.ERROR_REFUND_NUM);
		}
	}

	private RefundDto generateRefundDtoApply(Integer refundNum,OrderDetail orderDetail,String refundNo) {
		String orderNo = orderDetail.getOrderNo();
		//查询订单信息
		Order order = orderServiceImpl.getOrderByOrderNo(orderNo, null);
		//验证交易已完成
		if(0==order.getIsShow()){
			log.info("该订单不可退款");
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
		if(OrderType.PRE_SELL.getTypeCode().equals(order.getOrderType())){
			//预售订单
			orderNo = order.getPreOrderNo();
		}else{
			//现货订单
			orderNo = order.getOrderNo();
		}
		
		//应退款总额,(应付款金额-运费)*退款数量/总量,预售超过预售期为总额/2(非已付款或非已付定金不退还运费)
		//应付款金额 = 订单商品总金额+运费-折扣金额
		ChicunMoney refundMoney = new ChicunMoney(order.getOrderMoney());
		if(!OrderState.PAY_SUCCESS.getStateCode().equals(order.getOrderState())
				&&!OrderState.PRE_PAY_SUCCESS.getStateCode().equals(order.getOrderState())){
			refundMoney = refundMoney.subtract(order.getFreight());
		}
		refundMoney = refundMoney.multiply(refundNum).divide(orderDetail.getPtBuyNum());
		if(OrderType.PRE_SELL.getTypeCode().equals(order.getOrderType())){
			log.info("预售商品");
			Product t_product = productServiceImpl.getProductByProductNo(orderDetail.getProductNo());
			if(DateUtil.isAfter(t_product.getPreSellEndTime())){
				refundMoney = MoneyUtil.getHalfMoney(refundMoney.getMoney());
			}
		}
		
		RefundDto refundDto = new RefundDto();
		refundDto.setRefundNum(refundNum);
		refundDto.setRefundNo(refundNo);
		refundDto.setRefundMoney(refundMoney.getMoney());
		refundDto.setOrder(order);
		refundDto.setOrderDetail(orderDetail);
		
		return refundDto;
	}
	
	private RefundLog insertRefundLog(RefundDto refundDto,RecordType recordType) {
		Order order = refundDto.getOrder();
		OrderDetail orderDetail = refundDto.getOrderDetail();
		RefundLog refundLog = new RefundLog();
		refundLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		refundLog.setRefundNum(refundDto.getRefundNum());
		refundLog.setRecordType(recordType.getTypeCode());
		refundLog.setReason(refundDto.getReason());
		refundLog.setPictures(refundDto.getPictures());
		
		refundLog.setOrderDetailId(orderDetail.getId());
		refundLog.setOrderNo(order.getOrderNo());
		refundLog.setRefundNo(refundDto.getRefundNo());
		if(RecordType.APPLY.equals(recordType)){
			refundLog.setRefundState(RefundState.REFUND_APPROVEING.getStateCode());
		}else if(RecordType.DO.equals(recordType)){
			refundLog.setRefundState(RefundState.REFUNDING.getStateCode());
		}
		refundLog.setRefundType(refundDto.getRefundType().getTypeCode());
		refundLog.setPayTypeId(order.getPayId());
		refundLog.setCreateTime(DateUtil.getCurrentDate());
		refundLogMapper.insert(refundLog);
		return refundLog;
	}
	
	@Override
	public void refund(RefundReq req) {
		log.info("退款接口开始执行...");
		RefundLog refundLog = null;
		RefundDto refundDto = new RefundDto();
		if(RefundType.REFUND.name().equals(req.getRefundType())){
			Refund refund = getRefundByRefundNo(req.getRefundNo());
			//查询订单详细信息
			OrderDetail orderDetail = orderServiceImpl.getOrderDetailById(refund.getOrderDetailId());
			//生成逻辑dto
			refundDto = initRefundDto(refund.getRefundNum(),orderDetail,req.getRefundNo());
			refundDto.setRefundNo(req.getRefundNo());
			refundDto.setRefundType(RefundType.REFUND);
			refundDto.setRefund(refund);
			
			if(RefundState.REFUND_SUCCESS.getStateCode().equals(refund.getRefundState())){
				log.info("已成功退款");
				throw new DesignException(DesignEx.REFUND_NOT_APPROVE);
			}else if(!RefundState.REFUND_APPROVE_PASS.getStateCode().equals(refund.getRefundState())){
				log.info("退款状态异常,需要为审批通过");
				throw new DesignException(DesignEx.REFUND_NOT_APPROVE);
			}
			//初始化退款日志
			refundLog = insertRefundLog(refundDto,RecordType.DO);
		}else if(RefundType.RETURN.name().equals(req.getRefundType())){
			Returns returns = returnServiceImpl.getReturnByReturnNo(req.getRefundNo());
			//查询订单详细信息
			OrderDetail orderDetail = orderServiceImpl.getOrderDetailById(returns.getOrderDetailId());
			//生成逻辑dto
			refundDto = initRefundDto(returns.getReturnNum(),orderDetail,req.getRefundNo());
			refundDto.setRefundType(RefundType.RETURN);
			refundDto.setReturns(returns);
			Refund refund = insertRefund(refundDto,RefundState.REFUNDING);
			refundDto.setRefund(refund);
			//初始化退款日志
			refundLog = insertRefundLog(refundDto,RecordType.DO);
		}
		
		//验证是否可以退款
		log.info("验证是否可以退款");
		validateRefund(refundDto);
		
		try{
			log.info("准备退款业务逻辑");
			refundExecute(refundDto);
			refundLog.setRefundState(RefundState.REFUND_SUCCESS.getStateCode());
		}catch(Exception e){
			String errMsg = Throwables.getStackTraceAsString(e);
			log.info("退款异常：{}",errMsg);
			refundLog.setRefundState(RefundState.REFUND_FAIL.getStateCode());
			refundLog.setReason(errMsg.substring(0, Math.min(errMsg.length(), 200)));
			throw e;
		}finally{
			refundLog.setGuard(refundDto.getGuard());
			refundLog.setUpdateBy("refund");
			refundLog.setUpdateTime(DateUtil.getCurrentDate());
			refundLogMapper.update(refundLog);
		}
		
	}

	private void validateRefund(RefundDto refundDto) {
		Order order = refundDto.getOrder();
		OrderDetail orderDetail = refundDto.getOrderDetail();
		Integer orderState = order.getOrderState();
		Integer orderDetailState = orderDetail.getOrderDetailState();
		if(RefundType.REFUND.equals(refundDto.getRefundType())){
			//退款
			if(!OrderState.PAY_SUCCESS.getStateCode().equals(orderState)&&!OrderState.PRE_PAY_SUCCESS.getStateCode().equals(orderState)){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}else{
				if(OrderDetailState.REFUNDING.equals(orderDetailState)||OrderDetailState.CLOSE.equals(orderDetailState)){
					throw new DesignException(DesignEx.INTERNAL_ERROR);
				}
			}
		}else if(RefundType.RETURN.equals(refundDto.getRefundType())){
			//退货退款
			//退款
			if(!OrderState.REVEIVED.getStateCode().equals(orderState)){
				throw new DesignException(DesignEx.INTERNAL_ERROR);
			}else{
				if(OrderDetailState.REFUNDING.equals(orderDetailState)||OrderDetailState.CLOSE.equals(orderDetailState)){
					throw new DesignException(DesignEx.INTERNAL_ERROR);
				}
			}
		}
		
		Map<String,Object> param = Maps.newHashMap();
		param.put("orderNo", order.getOrderNo());
		List<Refund> refundList = getRefundList(param);
		Integer remainNum = orderDetail.getPtBuyNum();
		for(Refund tmp:refundList){
			if(tmp.getRefundNo().equals(refundDto.getRefundNo())){
				continue;
			}
			remainNum = remainNum - tmp.getRefundNum();
		}
		if(refundDto.getRefundNum()>remainNum){
			log.info("退换货个数错误,{}",refundDto.getRefundNum());
			throw new DesignException(DesignEx.ERROR_RETURN_NUM);
		}else if(refundDto.getRefundNum().equals(remainNum)){
			refundDto.setCanClose(true);
		}
	}



	private RefundDto initRefundDto(Integer refundNum, OrderDetail orderDetail,String refundNo) {
		RefundDto refundDto = generateRefundDtoApply(refundNum, orderDetail,refundNo);
		return DozerUtils.transfer(refundDto, RefundDto.class);
	}



	private void refundExecute(final RefundDto refundDto) {
		OrderDetail orderDetail = refundDto.getOrderDetail();
		Refund refund = refundDto.getRefund();
		
		ProductState productState = ProductState.getByState(orderDetail.getProductState());
		IRefundDetailService refundDetailService = refundDetailServices.get(productState.name());
		if(refundDetailService==null){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
		try{
			//退款
			refundDetailService.dealRefund(refundDto);
			
			//退款成功
			Order order = refundDto.getOrder();
			refund.setRefundState(RefundState.REFUND_SUCCESS.getStateCode());
			if(refundDto.isCanClose()){
				orderDetail.setOrderDetailState(OrderDetailState.CLOSE.getStateCode());
			}else{
				orderDetail.setOrderDetailState(OrderDetailState.REFUNDSUCCESS.getStateCode());
			}
			refundMapper.update(refund);
			orderServiceImpl.updateOrderDetailSteteAtRefund(orderDetail);
			orderServiceImpl.updateProductNumInStock(Constant.PUSH,refundDto.getRefundNum(),orderDetail.getPtstId());
			
			//关闭订单
			if(refundDto.isCanClose()){
				Integer refundedProductNum = refundMapper.getRefundProductNum(orderDetail.getOrderNo());
				Integer orderProductNum = orderServiceImpl.getOrderProductNum(orderDetail.getOrderNo());
				if(orderProductNum.equals(refundedProductNum)){
					order.setUpdateBy("refund");
					orderServiceImpl.closeOrder(order);
				}
			}
			//发送成功推送
			payLoadServiceImpl.exec(new IPayLoadStrategy() {
				
				@Override
				public PayLoad getPayLoad() {
					PayLoad payLoad = new PayLoad();
					payLoad.setPayLoadType(PayLoadType.DEFAULT.getTypeCode());
					payLoad.setTitle("退款成功");
					payLoad.setBody("退款金额："+refundDto.getRefundMoney());
					List<String> deviceTokens = Lists.newArrayList();
					deviceTokens.add(defaultUserServiceImpl.getUserByUserNo(refundDto.getOrder().getUserNo()).getDeviceToken());
					payLoad.setDeviceTokens(deviceTokens);
					return payLoad;
				}
			});
		}catch(Exception e){
			log.info("退款失败,{}",Throwables.getStackTraceAsString(e));
			refund.setRefundState(RefundState.REFUND_FAIL.getStateCode());
			orderDetail.setOrderDetailState(OrderDetailState.REFUNDFAIL.getStateCode());
			refundMapper.update(refund);
			orderServiceImpl.updateOrderDetailSteteAtRefund(orderDetail);
		}
		
		
		
	}
	
	@Override
	public List<RefundFollowResp> follow(Long orderDetailId) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("orderDetailId",orderDetailId);
		List<Refund> t_refunds = getRefundList(param);
		if(CollectionUtils.isNotEmpty(t_refunds)){
			Map<String,Object> params = Maps.newHashMap();
			String refundNo = t_refunds.get(0).getRefundNo();
			params.put("refundNo",refundNo);
			
			List<RefundLog> refundLogList =  refundLogMapper.getRefundLogList(params);
			List<RefundFollowResp> result = Lists.newArrayList();
			for(RefundLog refundLog :refundLogList){
				final RefundState refundState = RefundState.get(refundLog.getRefundState());
				RefundFollowResp followResp = DozerUtils.transfer(refundLog, RefundFollowResp.class, new MyTransfer<RefundLog, RefundFollowResp>() {
					@Override
					public void transfer(RefundLog from, RefundFollowResp to) {
						to.setRefundTime(from.getCreateTime());
						to.setRefundDesc(refundState.getStateDesc());
					}
				});
				result.add(followResp);
				if(RefundState.REFUND_APPROVEING.getStateCode().equals(refundLog.getRefundState())){
					//新增受理中记录
					RefundFollowResp tmp = new RefundFollowResp();
					try {
						tmp = (RefundFollowResp) followResp.clone();
					} catch (CloneNotSupportedException e) {
						
					}
					tmp.setRefundState("-2");
					tmp.setRefundDesc("受理中");
					tmp.setRefundTime(DateUtil.addSeconds(followResp.getRefundTime(), 5));
					result.add(tmp);
				}else if(RefundState.REFUND_APPROVE_PASS.getStateCode().equals(refundLog.getRefundState())){
					//新增受理中记录
					RefundFollowResp tmp = new RefundFollowResp();
					try {
						tmp = (RefundFollowResp) followResp.clone();
					} catch (CloneNotSupportedException e) {
						
					}
					tmp.setRefundState("-2");
					tmp.setRefundDesc("受理中");
					tmp.setRefundTime(DateUtil.addSeconds(followResp.getRefundTime(), 5));
					result.add(tmp);
				}
				
			}
			
			return result;
		}else{
			log.info("未发现退款记录");
			throw new DesignException(DesignEx.REFUND_NOT_EXISTS);
		}
	}

	@Override
	public List<Refund> getRefundList(Map<String, Object> param) {
		return refundMapper.getRefundList(param);
	}

	public Map<String, IRefundDetailService> getRefundDetailServices() {
		return refundDetailServices;
	}

	public void setRefundDetailServices(
			Map<String, IRefundDetailService> refundDetailServices) {
		this.refundDetailServices = refundDetailServices;
	}
	
}

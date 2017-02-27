package com.design.service.impl.returns;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.OrderDetailState;
import com.design.common.enums.OrderState;
import com.design.common.enums.RecordType;
import com.design.common.enums.RefundState;
import com.design.common.enums.ReturnState;
import com.design.common.enums.ReturnType;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.NoGenerator;
import com.design.common.utils.TraceLogIdUtils;
import com.design.dao.entity.Order;
import com.design.dao.entity.OrderDetail;
import com.design.dao.entity.Refund;
import com.design.dao.entity.ReturnLog;
import com.design.dao.entity.Returns;
import com.design.dao.persist.ReturnLogMapper;
import com.design.dao.persist.ReturnMapper;
import com.design.service.api.IAttachmentService;
import com.design.service.api.IRefundService;
import com.design.service.api.IReturnService;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.req.ReturnsApplyReq;
import com.design.service.impl.order.OrderServiceImpl;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.google.common.collect.Maps;

@Service
public class ReturnServiceImpl implements IReturnService{
	
	private static Logger log = LoggerFactory.getLogger(ReturnServiceImpl.class);
	
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private OrderServiceImpl orderServiceImpl;
	@Resource
	private IRefundService refundServiceImpl;
	@Resource
	private ReturnLogMapper returnLogMapper;
	@Resource
	private ReturnMapper returnMapper;
	@Resource
	private IAttachmentService attachmentServiceImpl;
	
	@Override
	@Transactional
	public void returnsApply(String token,ReturnsApplyReq req,List<AttachmentDto> attachmentDtos) {
		log.info("退换货接口开始执行...");
		defaultUserServiceImpl.getUserNoByToken(token);
		//查询订单详细信息
		OrderDetail orderDetail = orderServiceImpl.getOrderDetailById(req.getOrderDetailId());
		//生成逻辑dto
		ReturnsDto returnsDto = generateReturnDto(req.getReturnNum(), orderDetail,NoGenerator.generateRefundNo());
		returnsDto.setReturnType(req.getReturnType());
		returnsDto.setReason(req.getReason());
		if(CollectionUtils.isNotEmpty(attachmentDtos)){
			if(CollectionUtils.isNotEmpty(attachmentDtos)){
				List<Long> attachmentIds = attachmentServiceImpl.batchInsert(attachmentDtos);
				StringBuilder builder = new StringBuilder();
				for(Long tmp:attachmentIds){
					if(builder.length()>0){
						builder.append(",");
					}
					builder.append(tmp);
				}
				returnsDto.setPictures(builder.toString());
			}
		}
		//初始化退换货日志
		ReturnLog returnLog = insertReturnLog(returnsDto,RecordType.APPLY);
		returnsDto.setReturnLog(returnLog);
//		if(StringUtils.isNotEmpty(req.getReturnNo())){
//			Returns returns = returnMapper.getReturnByReturnNo(req.getReturnNo());
//			if(returns!=null){
//				if(ReturnState.RETURN_FAIL.getStateCode().equals(returns.getReturnState())
//						||ReturnState.EXCHANGE_FAIL.getStateCode().equals(returns.getReturnState())){
//					returns.setReturnState(ReturnState.RETURN_APPLYING.getStateCode());
//					returnMapper.update(returns);
//					returnLog.setReturnNo(returns.getReturnNo());
//					returnLog.setUpdateBy("returnApply");
//					returnLog.setUpdateTime(DateUtil.getCurrentDate());
//					returnLogMapper.update(returnLog);
//					return;
//				}else{
//					log.info("无退款请求号对应的退款");
//					throw new DesignException(DesignEx.NOT_REFUNDED);
//				}
//			}
//		}
		
		//验证是否可以退换货
		log.info("验证是否可以退换货");
		validateReturns (returnsDto);
		
		//初始化退换货表
		log.info("初始化退换货表");
		insertReturn(returnsDto);
		
		if(ReturnType.EXCHANGE.getTypeCode().equals(returnsDto.getReturnType()) ){
			orderDetail.setOrderDetailState(OrderDetailState.EXCHANGEING.getStateCode());
		}else if(ReturnType.RETURN.getTypeCode().equals(returnsDto.getReturnType()) ){
			orderDetail.setOrderDetailState(OrderDetailState.RETURNING.getStateCode());
		}
		orderServiceImpl.updateOrderDetailSteteAtRefund(orderDetail);
	}
	
	public void validateReturns(ReturnsDto returnsDto) {
		Order order = returnsDto.getOrder();
		OrderDetail orderDetail = returnsDto.getOrderDetail();
		Integer productNum = returnsDto.getReturnNum();
		Integer orderState = order.getOrderState();
		Integer orderDetailState = orderDetail.getOrderDetailState();
		
		if(!OrderState.REVEIVED.getStateCode().equals(orderState)){
			log.info("订单未收货");
			throw new DesignException(DesignEx.ERROR_RETURN_ORDER_STATE);
		}else{
			if(OrderDetailState.EXCHANGEING.getStateCode().equals(orderDetailState)
					||OrderDetailState.RETURNING.getStateCode().equals(orderDetailState)){
				log.info("订单正在退换货");
				throw new DesignException(DesignEx.RETURNING);
			}else if(OrderDetailState.CLOSE.getStateCode().equals(orderDetailState)){
				log.info("订单已关闭");//can not get here
				throw new DesignException(DesignEx.ORDER_CLOSED);
			}
		}
		//验证数量
		Map<String,Object> param = Maps.newHashMap();
		param.put("orderNo", order.getOrderNo());
		param.put("orderDetailId",orderDetail.getId());
		List<Refund> refundList = refundServiceImpl.getRefundList(param);
		
		Integer remainNum = orderDetail.getPtBuyNum();
		for(Refund tmp:refundList){
			remainNum = remainNum - tmp.getRefundNum();
			if(!RefundState.REFUND_SUCCESS.getStateCode().equals(tmp.getRefundState())){
				log.info("存在未退款成功记录,{}",tmp);
				throw new DesignException(DesignEx.NOT_REFUNDED);
			}
		}
		
		List<Returns> returnsList = returnMapper.getReturnsList(param);
		for(Returns tmp:returnsList){
			if(!ReturnState.RETURN_SUCCESS.getStateCode().equals(tmp.getReturnState())
					||!ReturnState.EXCHANGE_SUCCESS.getStateCode().equals(tmp.getReturnState())){
				log.info("存在未退换货成功记录,{}",tmp);
				throw new DesignException(DesignEx.RETURNING);
			}
		}
		if(productNum>remainNum){
			log.info("退换货个数错误,{}",productNum);
			throw new DesignException(DesignEx.ERROR_RETURN_NUM);
		}else if(productNum.equals(remainNum)){
			returnsDto.setCanClose(true);
		}
}

	private Returns insertReturn(ReturnsDto returnsDto) {
		Order order = returnsDto.getOrder();
		OrderDetail orderDetail = returnsDto.getOrderDetail();
		Returns returns = new Returns();
		returns.setUserNo(order.getUserNo());
		returns.setOrderNo(order.getOrderNo());
		returns.setOrderDetailId(orderDetail.getId());
		returns.setReturnNo(returnsDto.getReturnNo());
		returns.setReturnNum(returnsDto.getReturnNum());
		returns.setReturnType(returnsDto.getReturnType());
		returns.setReturnState(ReturnState.RETURN_APPLYING.getStateCode());
		returns.setCreateTime(DateUtil.getCurrentDate());
		returnMapper.insert(returns);
		return returns;
	}

	private ReturnsDto generateReturnDto(Integer returnNum,OrderDetail orderDetail,String returnNo) {
		String orderNo = orderDetail.getOrderNo();
		//查询订单信息
		Order order = orderServiceImpl.getOrderByOrderNo(orderNo, null);
		ReturnsDto returnsDto = new ReturnsDto();
		returnsDto.setReturnNum(returnNum);
		returnsDto.setOrder(order);
		returnsDto.setOrderDetail(orderDetail);
		returnsDto.setReturnNo(returnNo);
		return returnsDto;
	}

	private ReturnLog insertReturnLog(ReturnsDto returnsDto,RecordType recordType) {
		OrderDetail orderDetail = returnsDto.getOrderDetail();
		Order order = returnsDto.getOrder();
		ReturnLog returnLog = new ReturnLog();
		returnLog.setTraceLogId(TraceLogIdUtils.getTraceLogId());
		returnLog.setReturnNum(returnsDto.getReturnNum());
		returnLog.setRecordType(recordType.getTypeCode());
		returnLog.setReason(returnsDto.getReason());
		returnLog.setPictures(returnsDto.getPictures());
		
		returnLog.setOrderDetailId(orderDetail.getId());
		returnLog.setOrderNo(order.getOrderNo());
		returnLog.setReturnNo(returnsDto.getReturnNo());
		returnLog.setReturnState(ReturnState.RETURN_APPLYING.getStateCode());
		returnLog.setReturnType(returnsDto.getReturnType());
		returnLog.setCreateTime(DateUtil.getCurrentDate());
		returnLogMapper.insert(returnLog);
		return returnLog;
	}


	public Returns getReturnByReturnNo(String refundNo) {
		Returns returns = returnMapper.getReturnByReturnNo(refundNo);
		if(returns==null){
			throw new DesignException(DesignEx.RETURN_NOT_EXISTS);
		}
		return returns;
	}

}

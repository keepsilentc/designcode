package com.design.service.api;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.Refund;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.TradeMoneyRemainDto;
import com.design.service.api.dto.req.RefundApplyReq;
import com.design.service.api.dto.req.RefundReq;
import com.design.service.api.dto.resp.RefundFollowResp;


public interface IRefundService {

	void refund(RefundReq req);

	void refundApply(String token, RefundApplyReq req,List<AttachmentDto> attachmentDtos);

	List<RefundFollowResp> follow(Long orderDetailId);

	List<Refund> getRefundList(Map<String, Object> param);

	TradeMoneyRemainDto getTradeMoneyReamin(String orderNo, String refundNo);

}

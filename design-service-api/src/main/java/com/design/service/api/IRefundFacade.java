package com.design.service.api;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.Refund;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.req.RefundApplyReq;
import com.design.service.api.dto.req.RefundReq;
import com.design.service.api.dto.resp.RefundFollowResp;

public interface IRefundFacade {
	
	void refund(RefundReq req);

	List<RefundFollowResp> follow(Long orderDetailId);

	List<Refund> getRefundList(Map<String, Object> param);
	
	void refundApply(String token, RefundApplyReq req, List<AttachmentDto> attachmentDtos);

}

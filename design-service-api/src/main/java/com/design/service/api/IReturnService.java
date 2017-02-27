package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.AttachmentDto;
import com.design.service.api.dto.req.ReturnsApplyReq;

public interface IReturnService {

	void returnsApply(String token, ReturnsApplyReq req, List<AttachmentDto> attachmentDtos);

}

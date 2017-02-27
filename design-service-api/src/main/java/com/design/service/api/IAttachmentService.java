package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.AttachmentDto;

public interface IAttachmentService {

	List<Long> batchInsert(List<AttachmentDto> attachmentDtos);


}

package com.design.service.impl.attachment;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.stereotype.Service;

import com.design.common.utils.DozerUtils;
import com.design.dao.entity.Attachment;
import com.design.dao.persist.AttachmentMapper;
import com.design.service.api.IAttachmentService;
import com.design.service.api.dto.AttachmentDto;

@Service
public class AttachmentServiceImpl implements IAttachmentService {

	@Resource
	private AttachmentMapper attachmentMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> batchInsert(List<AttachmentDto> attachmentDtos) {
		List<Attachment> attachments = DozerUtils.transferList(attachmentDtos, Attachment.class);
		attachmentMapper.batchInsert(attachments);
		
		return (List<Long>) CollectionUtils.collect(attachments, new Transformer() {
			
			@Override
			public Object transform(Object input) {
				return ((Attachment)input).getId();
			}
		});
	}

}

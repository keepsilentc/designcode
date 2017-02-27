package com.design.dao.persist;

import java.util.List;

import com.design.dao.entity.Attachment;


public interface AttachmentMapper {
	
	void insert(Attachment attachment);

	Attachment getAttachmentById(Long id);

	int update(Attachment attachment);

	void batchInsert(List<Attachment> attachments);
	
}

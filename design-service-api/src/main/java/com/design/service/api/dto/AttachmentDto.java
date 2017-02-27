package com.design.service.api.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttachmentDto {

	private String classify;
	private String fileName;
	private String filePath;
	private String serverFileName;
	private Date createTime;

}

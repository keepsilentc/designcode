package com.design.service.impl.aliyun;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.aliyun.oss.model.CompleteMultipartUploadResult;

@Getter
@ToString
public class ChicunAliFile {
	private String uploadFile;
	private String key;
	@Setter
	private CompleteMultipartUploadResult result;
	
	public ChicunAliFile(String uploadFile, String key) {
		this.uploadFile = uploadFile;
		this.key = key;
	}
	
}

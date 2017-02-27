package com.design.service.impl.user;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.DateUtil;
import com.design.dao.entity.Attachment;
import com.design.dao.entity.User;
import com.design.dao.persist.AttachmentMapper;
import com.design.service.api.dto.AttachmentDto;
import com.design.service.impl.aliyun.AliyunServiceImpl;
import com.design.service.impl.aliyun.ChicunAliFile;
import com.google.common.collect.Lists;
@Service
public class UploadServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
	
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	
	@Resource
	private AttachmentMapper attachmentMapper;
	
	@Resource
	private AliyunServiceImpl aliyunServiceImpl;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Value("${upload.temp.path}")
	private String uploadTempPath;
	
	@Transactional
	public void uploadavatar(String token, CommonsMultipartFile file) throws IOException {
		User t_user = defaultUserServiceImpl.getUserByToken(token);
		String originalfileName = file.getOriginalFilename();
		String suffix = file.getOriginalFilename().substring(originalfileName.lastIndexOf("."), originalfileName.length());
		String newName = DateUtil.getCurrent(DateUtil.ALL_PATTERN)+new Random(UUID.randomUUID().hashCode()).nextInt(99)+suffix;
		String filePath = uploadPath+"avatar"+File.separator;
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath+newName));
		Attachment attachment = new Attachment();
		attachment.setFileName(originalfileName);
		attachment.setFilePath(filePath);
		attachment.setClassify("avatar");
		attachment.setServerFileName(newName);
		attachment.setCreateTime(DateUtil.getCurrentDate());
		attachmentMapper.insert(attachment);
		t_user.setPhoto(attachment.getId());
		defaultUserServiceImpl.updateUserById(t_user);
		aliyunServiceImpl.uploadFiles(new ChicunAliFile(filePath+newName,String.valueOf(attachment.getId())));
	}

	
	public File getFile(Long id) {
		Attachment t_Attachment =  attachmentMapper.getAttachmentById(id);
		if(t_Attachment==null){
			throw new DesignException(DesignEx.ATTACHMENET_NOTFIND);
		}
		File file = new File(t_Attachment.getFilePath()+t_Attachment.getServerFileName());
		if(file.exists()){
			return file;
		}
		throw new DesignException(DesignEx.FILE_NOTEXISTS);
	}


	public List<AttachmentDto> uploadFile(MultipartFile[] pictures,String classify) throws IOException {
		List<AttachmentDto> attachmentDtos = Lists.newArrayListWithCapacity(pictures.length);
		MultipartFile multipartFile = null;
		String originFileName = null;
		AttachmentDto attachment = null;
		for(int i=0,len=pictures.length;i<len;i++){
			multipartFile = pictures[i];
			originFileName = multipartFile.getOriginalFilename();
			String suffix = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
			String newName = DateUtil.getCurrent(DateUtil.ALL_PATTERN)+new Random(UUID.randomUUID().hashCode()).nextInt(99)+suffix;
			String filePath = uploadPath+classify+File.separator;
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(filePath+newName));
			attachment  = new AttachmentDto();
			attachment.setClassify(classify);
			attachment.setFileName(originFileName);
			attachment.setFilePath(filePath);
			attachment.setServerFileName(newName);
			attachment.setCreateTime(DateUtil.getCurrentDate());
			attachmentDtos.add(attachment);
		}
		return attachmentDtos;
	}


	public void removeFile(List<AttachmentDto> attachmentDtos) {
		File file = null;
		for(AttachmentDto attachment:attachmentDtos){
			file = new File(attachment.getFilePath()+attachment.getServerFileName());
			if (file.isFile() && file.exists()) {  
				log.info("删除文件,{}",attachment.getFileName());
		        file.delete();  
		    }
		}
	}
	
}

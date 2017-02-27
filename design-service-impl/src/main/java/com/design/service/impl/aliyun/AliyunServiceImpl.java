package com.design.service.impl.aliyun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import com.design.common.assist.DesignException;
import com.google.common.base.Throwables;

@Service
public class AliyunServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(AliyunServiceImpl.class);
	
	@Value("${aliyun.endpoint}")
	private String endpoint;
	
	@Value("${aliyun.accessKeyId}")
    private String accessKeyId;
	
	@Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
	
	@Value("${aliyun.bucketName}")
    private String bucketName;
	
    private ThreadLocal<OSSClient> ossclientLocal = new ThreadLocal<OSSClient>();
    
	public OSSClient getOssClient(){
		OSSClient ossclient = ossclientLocal.get();
		if(ossclient==null){
			ossclient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			ossclientLocal.set(ossclient);
		}
		return ossclient;
	}
	
	public void closeOssclient(){
		getOssClient().shutdown();
	}
	
	public void uploadFiles(ChicunAliFile ...files){
		OSSClient ossClient = getOssClient();
		try{
			for(ChicunAliFile chicunFile:files){
				UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName,"chicun/"+chicunFile.getKey());
				// 待上传的本地文件
                uploadFileRequest.setUploadFile(chicunFile.getUploadFile());
                //文件上传
				UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
				CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
				chicunFile.setResult(multipartUploadResult);
			}
		} catch (OSSException oe) {
			log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
			log.info("Error Message: " + oe.getErrorCode());
			log.info("Error Code:       " + oe.getErrorCode());
			log.info("Request ID:      " + oe.getRequestId());
			log.info("Host ID:           " + oe.getHostId());
			throw oe;
        } catch (ClientException ce) {
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message: " + ce.getMessage());
            throw ce;
        } catch (Throwable e) {
            throw new DesignException(Throwables.getStackTraceAsString(e));
        }finally{
			closeOssclient();
		}
	}
	
}

package com.design.service.impl.padload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.teaey.apns4j.Apns4j;
import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.protocol.ApnsPayload;

import com.design.common.assist.DesignException;
import com.design.service.api.dto.payload.PayLoad;

@Service
public class PayLoadUtil {
	
	private static Logger log = LoggerFactory.getLogger(PayLoadUtil.class);
	
	private ApnsChannel apnsChannel;
	
	@Value("${keystore.path}")
	private String keyStoreMeta;
	
	@Value("${keystore.password}")
	private String keyStorePwd;
	
	
	private ApnsChannel getChannel(){
		if(apnsChannel==null){
			synchronized (this) {
				ApnsChannelFactory apnsChannelFactory = Apns4j.newChannelFactoryBuilder()
						.keyStoreMeta(PayLoadUtil.class.getClassLoader().getResourceAsStream(keyStoreMeta))
						.keyStorePwd(keyStorePwd)
						.build();
				apnsChannel = apnsChannelFactory.newChannel();
			}
		}
		return apnsChannel;
	}
	
	public void send(ApnsPayload apnsPayload,String ...deviceTokens){
		for(String deviceToken:deviceTokens){
			ApnsChannel apnsChannel = getChannel();
			try{
				log.info("推送：{}",deviceToken);
				apnsChannel.send(deviceToken, apnsPayload);
			} catch (Exception e) {
				throw new DesignException(e.getMessage());
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	private ApnsPayload demo(){
		ApnsPayload apnsPayload = Apns4j.newPayload()
				.badge(1)
		        .alertTitle("Title")
		        .alertBody("Pushed by apns4j")
		        .sound("default");
		return apnsPayload;
	}

	public void send(PayLoad vo) {
		ApnsPayload payload = Apns4j.newPayload()
				.badge(1)
		        .alertTitle(vo.getTitle())
		        .alertBody(vo.getBody())
		        .sound("default")
		        .extend("messageType", vo.getPayLoadType())
		        .extend("identify", vo.getIdntify());
		send(payload,vo.getDeviceTokens().toArray(new String[0]));
		log.info("发送完毕");
		
	}
}

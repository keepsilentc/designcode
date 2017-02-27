package com.design.service.impl.padload;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.google.common.collect.Maps;

@Service
public class SendSms {
	
	private static Logger log = LoggerFactory.getLogger(SendSms.class);
	
	@Value("${sms.account}")
	private String account;
	
	@Value("${sms.passwd}")
	private String passwd;
	
	@Value("${sms.url}")
	private String smsUrl;
	
	public static final String DEF_CHATSET = "UTF-8";

    public static final int DEF_CONN_TIMEOUT = 10000;

    public static final int DEF_READ_TIMEOUT = 10000;
    
	public void sendSms(String msg,String mobile){
		Map<String,String> params = Maps.newHashMap();//请求参数

        params.put("account",account); //用户产品账号  短信 语音 国际 不同验证码 换不同账号即可

        params.put("pswd",passwd);//用户产品密码

        params.put("mobile",mobile); //手机号码 多手机号用逗号分隔如 13300000000,13200000000
        
        
        params.put("msg", msg);//必填参数。短信内容(内容后面必须要带上签名如:[示远科技])
        
        
        params.put("needstatus", "true");//是否需要状态报告，取值true或false，true，表明需要状态报告；false不需要状态报告needstatus参数需要等于true才会返回msgid
        
        
        params.put("product", "");//可选参数。用户订购的产品id，不填写（针对老用户）系统采用用户的默认产品，用户订购多个产品时必填，否则会发生计费错误。
        
        try {
        	
        	URL url = new URL(smsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            StringBuilder builder = new StringBuilder();
            for (Entry<String, String> i : params.entrySet()) {
            	builder.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue(),"UTF-8")).append("&");
            }
            OutputStream os = conn.getOutputStream();
            os.write(builder.toString().getBytes(DEF_CHATSET));
            InputStream ins = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, DEF_CHATSET));

            String strRead = null;
            if(ins.available()!=-1){
            	if ((strRead = reader.readLine()) != null) {
            		String[] result = strRead.split(",");
            		if(!"0".equals(result[1])){
            			log.info(strRead);
            			throw new DesignException(DesignEx.SMS_SEND_FAIL);
            		}
                }
            };
        } catch (Exception e) {
            throw new DesignException(e.getMessage());
        }
	}
	

}

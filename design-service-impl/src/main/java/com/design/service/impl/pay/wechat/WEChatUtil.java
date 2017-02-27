package com.design.service.impl.pay.wechat;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.StringUtils;
import com.design.common.assist.DesignException;
import com.design.common.utils.HttpUtil;
import com.design.common.utils.Md5Util;
import com.design.service.api.dto.pay.wechat.WxCloseReq;
import com.design.service.api.dto.pay.wechat.WxCloseResp;
import com.design.service.api.dto.pay.wechat.WxQueryReq;
import com.design.service.api.dto.pay.wechat.WxQueryResp;
import com.design.service.api.dto.pay.wechat.WxRefundReq;
import com.design.service.api.dto.pay.wechat.WxRefundResp;
import com.design.service.api.dto.pay.wechat.WxUnifiedOrderReq;
import com.design.service.api.dto.pay.wechat.WxUnifiedOrderResp;
import com.design.service.api.dto.pay.wechat.WxcommonReq;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service
public class WEChatUtil {
	
	private static Logger log = LoggerFactory.getLogger(WEChatUtil.class);
	//应用ID
	@Value("${wechat.app_id}")
	private String appid;
	//商户号
	@Value("${wechat.mch_id}")
	private String mch_id;
	//商户号
	@Value("${wechat.key}")
	private String key;
	//微信回掉通知地址
	@Value("${wechat.notify}")
	private String notifyUrl;
	//微信证书路径
	@Value("${wechat.certpath}")
	private String certPath;
	
	private SSLConnectionSocketFactory sslsf;
	
	@SuppressWarnings("deprecation")
	private SSLConnectionSocketFactory getSSLF(){
		if(sslsf==null){
			synchronized (this) {
				if(sslsf==null){
					try {
						KeyStore keyStore  = KeyStore.getInstance("PKCS12");
						InputStream instream = FileUtils.openInputStream(new File(certPath));
						try {
						    keyStore.load(instream,this.mch_id.toCharArray());
						} finally {
						    instream.close();
						}

						// Trust own CA and all self-signed certs
						SSLContext sslcontext = SSLContexts.custom()
						        .loadKeyMaterial(keyStore, this.mch_id.toCharArray())
						        .build();
						// Allow TLSv1 protocol only
						this.sslsf = new SSLConnectionSocketFactory(
						        sslcontext,
						        new String[] { "TLSv1" },
						        null,
						        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
					} catch (Exception e) {
						log.info("获取微信证书ssl失败");
						throw new DesignException(e.getMessage());
					}
				}
			}
		}
		return sslsf;
	}
	
	public void setCommonReq(WxcommonReq<?> req){
		req.setAppid(appid);
		req.setMch_id(mch_id);
		req.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
	}
	
	public WxUnifiedOrderResp unifiedorder(WxUnifiedOrderReq req){
		setCommonReq(req);
		req.setSpbill_create_ip("127.0.0.1");
		req.setNotify_url(notifyUrl);
		req.setTrade_type("APP");
		Map<String, String> param = beanToMap(req);
		req.setSign(sign(param));
		
		WxUnifiedOrderResp resp = null;
		XStream xStream = new XStream(new XppDriver(){
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out){

					@Override
					public String encodeNode(String name) {
						return name;
					}
				};
			}
			
		});
        xStream.autodetectAnnotations(true);
        String xml = xStream.toXML(req);
		
        String result = HttpUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml,"application/xml");
		log.info("结果,{}",result);
        xStream.processAnnotations(WxUnifiedOrderResp.class);
		xStream.autodetectAnnotations(true);
		resp = (WxUnifiedOrderResp) xStream.fromXML(result);
		return resp;
	}

	public Map<String, String> beanToMap(Object req) {
		Map<String, String> param = Maps.newTreeMap();
		try {
			BeanInfo beaninfo = Introspector.getBeanInfo(req.getClass(), Object.class);
			PropertyDescriptor[] propertyDescriptors = beaninfo.getPropertyDescriptors();
			for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
				String key = propertyDescriptor.getName();
				Object value =  propertyDescriptor.getReadMethod().invoke(req);
				if (null!=key&&null!=value) {
					param.put(key,String.valueOf(value));
				}
			}
		}catch (Exception e) {
			throw new DesignException(e.getMessage());
		}
		return param;
	}
	
	public String sign(Object req) {
		return sign(beanToMap(req));
	}
	
	
	public String sign(Map<String,String> param) {
		String content = getSignContent(param)+"&key="+key;
		log.info("参与signcontet,{}",content);
		String sign = null;
		try {
			sign = Md5Util.string2MD5(new String(content.getBytes(),"ISO8859-1")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			throw new DesignException(Throwables.getStackTraceAsString(e));
		}
		return sign;
	}
	
	
	
	/**
     * 
     * @param sortedParams
     * @return
     */
    private String getSignContent(Map<String, String> params) {
    	StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (StringUtils.areNotEmpty(key, value)&&!"sign".equals(key)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

	public WxRefundResp tradeRefund(WxRefundReq req) {
		try{
			HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
		    log.info("微信请求...{}",WxXmlUtil.toXml(req));
			httppost.setEntity(EntityBuilder.create().setText(WxXmlUtil.toXml(req)).build());
		    CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLSocketFactory(getSSLF())
	                .build();
		    String result = httpPost(httppost,httpClient);
		    log.info("微信返回...{}",result);
		    return WxXmlUtil.fromXml(result, WxRefundResp.class);
		}catch (Exception e) {
			log.info("微信退款执行异常");
			throw new DesignException(e.getMessage());
		}
	}

	public WxQueryResp tradeQuery(WxQueryReq req) {
		try{
			String reqStr = WxXmlUtil.toXml(req);
			log.info("微信请求...{}",reqStr);
			String result = HttpUtil.post("https://api.mch.weixin.qq.com/pay/orderquery",reqStr,"application/xml");
		    log.info("微信返回...{}",result);
		    return WxXmlUtil.fromXml(result, WxQueryResp.class);
		}catch (Exception e) {
			log.info("微信退款执行异常");
			throw new DesignException(e.getMessage());
		}
	}

	public WxCloseResp tradeClose(WxCloseReq req) {
		try{
			String reqStr = WxXmlUtil.toXml(req);
			log.info("微信请求...{}",reqStr);
			String result = HttpUtil.post("https://api.mch.weixin.qq.com/pay/closeorder",reqStr,"application/xml");
		    log.info("微信返回...{}",result);
		    return WxXmlUtil.fromXml(result, WxCloseResp.class);
		}catch (Exception e) {
			log.info("微信退款执行异常");
			throw new DesignException(e.getMessage());
		}
	}
	
	/** 
     * 处理Http请求 
     *  
     * @param request 
     * @return 
     */  
    private static String getResult(HttpRequestBase request,CloseableHttpClient httpClient) {  
       
    	try {
			CloseableHttpResponse response = httpClient.execute(request);
			try {
			    HttpEntity entity = response.getEntity();
			    System.out.println("----------------------------------------");
			    System.out.println(response.getStatusLine());
			    if (entity != null) {
			        System.out.println("Response content length: " + entity.getContentLength());
			        String result = EntityUtils.toString(entity,"UTF-8");  
			        return result; 
			    }
			    EntityUtils.consume(entity);
			    return "";
			} finally {
			    response.close();
			}
		}catch (Exception e) {
			throw new DesignException(e.getMessage());
		}
    	
    }

	public static String httpPost(HttpPost httppost,CloseableHttpClient httpClient) {
		return getResult(httppost,httpClient);
	}
	
}

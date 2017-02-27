package com.design.service.impl.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.StringUtils;
import com.google.common.base.Throwables;

@Component
public class AlipayUtil {
	
	private static Logger log = LoggerFactory.getLogger(AlipayUtil.class);
	
	private AlipayClient client;

	@Value("${alipay.app_id}")
	private String app_id;
	
	@Value("${alipay.seller_id}")
	private String seller_id;
	
	@Value("${alipay.open_api_domain}")
	private String openApiDomain;

	@Value("${alipay.app_private_key}")
	private String appPrivateKey;

	@Value("${alipay.alipay_public_key}")
	private String alipayPublicKey;

	@Value("${alipay.format}")
	private String format;

	@Value("${alipay.charset}")
	private String charset;

	private String sign_type = AlipayConstants.SIGN_TYPE_RSA;

//	private String encryptType = AlipayConstants.ENCRYPT_TYPE_AES;

//	private String encryptKey;

	public AlipayClient getClient() {
		if (client == null) {
			synchronized (this) {
				if (client == null) {
					client = new DefaultAlipayClient(openApiDomain, app_id,
							appPrivateKey, format, charset, alipayPublicKey);
				}
			}
		}
		return client;
	}
	
	public AlipayTradeQueryResponse tradeQuery(String outTradeNo,String tradeNo){
		AlipayTradeQueryRequest req = new AlipayTradeQueryRequest();
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setTradeNo(tradeNo);
		model.setOutTradeNo(outTradeNo);
		req.setBizModel(model);
		try {
			AlipayTradeQueryResponse resp = getClient().execute(req);
			if("ACQ.SYSTEM_ERROR".equals(resp.getSubCode())){
				return tradeQuery(outTradeNo,tradeNo);
			}
			return resp;
		} catch (AlipayApiException e) {
			log.info("alipay异常,{}",Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
	}
	
	public AlipayTradeCloseResponse tradeClose(String outTradeNo, String tradeNo) {
		AlipayTradeCloseRequest req = new AlipayTradeCloseRequest();
		AlipayTradeCloseModel model = new AlipayTradeCloseModel();
		model.setOutTradeNo(outTradeNo);
		model.setTradeNo(tradeNo);
		req.setBizModel(model);
		try {
			AlipayTradeCloseResponse resp = getClient().execute(req);
			if("ACQ.SYSTEM_ERROR".equals(resp.getSubCode())){
				return tradeClose(outTradeNo,tradeNo);
			}
			return resp;
		} catch (AlipayApiException e) {
			log.info("alipay异常,{}",Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
	}
	
//	public static void main(String[] args) throws FileNotFoundException, IOException {
////		List<String> ss = IOUtils.readLines(AlipayUtil.class.getResourceAsStream("order.txt"));
//		List<String> ss = Lists.newArrayList();
////		ss.add("2017011517113960406");
////		ss.add("");
////		ss.add("");
//		AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2017011305062667",
//				"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJkDuOIu0IZWDUKitMdjmsgFZ13dDO40B1oy/UZtFw+RStnq7XVhgwQ7O0v3XKoiS3V/1l+WzNKxDWl9PZ0d+NtZwTpReG/sFo+mdMUjpFbr2L8UenbeZ/weJSv8SiIwiDHQscvHFc0b55Q1KMaxWlIOUXOQX1U0yQmqdscUeeo1AgMBAAECgYB4mcxwyUElgFF3QdKGjOUD3e54tq9oV62r1CB4D6drZ7K9S0LZj3+6KA02dMKefeVHuPh34t2Q+md9y25ylZr74BlvQigpDuzoW+Y5QWr1zu73QtScLd379ConNW1jpu0iR3gSe1GFyTZ99LdtdCCSmCFfKt0iAStRGtPoG6k+gQJBAMmVRoAxtAgiwcnFXaTVS+emowpEJ2CEfjkAZwd2wXEYg9/9OR7rONYgZgOIYvyGxVTk7NZg7YR85bzeicnUVoUCQQDCUgm0l/KepaaWFpfwBha7p7Sv9op58VwViz5r4Iaj5YI0fhYVpUGAuVZclCg3UbKQtIV5+FCP7YclyAp25cvxAkBlm7K/p04B09Muayd6kHEEQQBpilT215HwFX3ZS0jSQvMmEjOanCI3/HivSy3p79zDn+ipXo4afAIpI1r8uuUpAkBtNW3+8OaKAs78yvXZD5L3I+mIeyaYIe1+riMGvcjtk675kWMYBygpEABZR2rSrDeO2+WoBsZAHV42/ZXj/IIBAkEAkuNK814CxIEyM7AuK300MYFx4fXz5FHeY0CDbreqNkMtQr8TaCnGAFs6rbQnJDLpbHBolYfkGAuKEML+fJZa/Q==",
//				"json", "utf-8", 
//				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB");
//		for(String s :ss){
//			AlipayTradeRefundRequest req = new AlipayTradeRefundRequest();
//			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
//			model.setOutRequestNo(NoGenerator.generateRefundNo());
//			model.setOutTradeNo(s);
//			model.setRefundAmount("0.01");
//			req.setBizModel(model);
//			
//			try {
//				AlipayTradeRefundResponse resp = client.execute(req);
//			} catch (AlipayApiException e) {
//				log.info("alipay异常,{}",Throwables.getStackTraceAsString(e));
//				throw new DesignException(DesignEx.INTERNAL_ERROR);
//			}
//		}
//		
//	}
	
	public AlipayTradeRefundResponse tradeRefund(String outTradeNo, String tradeNo,String outRequestNo,BigDecimal refundAmount) {
		AlipayTradeRefundRequest req = new AlipayTradeRefundRequest();
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setOutRequestNo(outRequestNo);
		model.setOutTradeNo(outTradeNo);
		model.setTradeNo(tradeNo);
		model.setRefundAmount(refundAmount.toString());
		req.setBizModel(model);
		
		try {
			AlipayTradeRefundResponse resp = getClient().execute(req);
			if("ACQ.SYSTEM_ERROR".equals(resp.getSubCode())){
				return tradeRefund(outTradeNo,tradeNo,outRequestNo,refundAmount);
			}
			return resp;
		} catch (AlipayApiException e) {
			log.info("alipay异常,{}",Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
	}

	public boolean rsaCheckV1(Map<String, String> params) throws AlipayApiException {
		return AlipaySignature.rsaCheckV1(params, this.alipayPublicKey, this.charset);
	}

	public String rsaSign(Map<String, String> params) throws AlipayApiException {
		params.put(AlipayConstants.APP_ID, this.app_id);
		params.put(AlipayConstants.METHOD, "alipay.trade.app.pay");
		params.put(AlipayConstants.VERSION, "1.0");
		params.put(AlipayConstants.CHARSET, this.charset);
		params.put(AlipayConstants.SIGN_TYPE, this.sign_type);
		Long timestamp = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat(AlipayConstants.DATE_TIME_FORMAT);
		df.setTimeZone(TimeZone.getTimeZone(AlipayConstants.DATE_TIMEZONE));
		params.put(AlipayConstants.TIMESTAMP,df.format(new Date(timestamp)));
		
		params.put(AlipayConstants.SIGN,AlipaySignature.rsaSign(params, this.appPrivateKey,this.charset));
		
		
		StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int index = 0;
        try {
			for (int i = 0; i < keys.size(); i++) {
			    String key = keys.get(i);
			    String value = params.get(key);
			    if (StringUtils.areNotEmpty(key, value)) {
			        content.append((index == 0 ? "" : "&") + key + "=" + URLEncoder.encode(value,"UTF-8"));
			        index++;
			    }
			}
		} catch (UnsupportedEncodingException e) {
			throw new DesignException(Throwables.getStackTraceAsString(e));
		}
        return content.toString();
	}

	public String getApp_id() {
		return app_id;
	}

	public String getOpenApiDomain() {
		return openApiDomain;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public String getFormat() {
		return format;
	}

	public String getCharset() {
		return charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public String getSeller_id() {
		return seller_id;
	}
	
}

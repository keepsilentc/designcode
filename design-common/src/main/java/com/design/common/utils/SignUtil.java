package com.design.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SignUtil {
	
	public static String sign(Map<String,String> param) {
		String key = param.get("key");
		if(StringUtils.isEmpty(key)){
			throw new NullPointerException("key");
		}
		param.remove("key");
		if(param.get("nonce_str")==null){
			param.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
		}
		String content = getSignContent(param)+"&key="+key;
		param.put("key", key);
		return Md5Util.string2MD5(content).toUpperCase();
	}
	
	/**
     * 
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();	
    }
}	

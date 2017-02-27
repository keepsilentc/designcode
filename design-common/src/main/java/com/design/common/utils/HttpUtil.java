package com.design.common.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import com.design.common.assist.DesignException;

public class HttpUtil {
	public static final String DEF_CHATSET = "UTF-8";
	
	private static final String     METHOD_POST     = "POST";
	
//    private static final String     METHOD_GET      = "GET";

    public static final int DEF_CONN_TIMEOUT = 10000;

    public static final int DEF_READ_TIMEOUT = 10000;
    
    private HttpUtil(){}
    
    
	private static HttpURLConnection getConnection(URL url, String method,String ctype) throws IOException{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
		conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}
    
	
    public static String paramToQueryString(Map<String, String> params, String charset) {
        
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder paramString = new StringBuilder();
        boolean first = true;
        for(Entry<String, String> p : params.entrySet()) {
            String key = p.getKey();
            String value = p.getValue();

            if (!first) {
                paramString.append("&");
            }

            paramString.append(urlEncode(key, charset));
            if (value != null) {
                paramString.append("=").append(urlEncode(value, charset));
            }

            first = false;
        }

        return paramString.toString();
    }
    
    public static String urlEncode(String value, String encoding) {
        if (value == null) {
            return "";
        }
        
        try {
            String encoded = URLEncoder.encode(value, encoding);
            return encoded.replace("+", "%20").replace("*", "%2A")
                    .replace("~", "%7E").replace("/", "%2F");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("encoding error",e);
        }
    }
    
    public static String post(String urlStr,Map<String,String> params){
    	String ctype = "application/x-www-form-urlencoded;charset="+DEF_CHATSET;
		return post(urlStr,paramToQueryString(params,"UTF-8"),ctype);
	}
    
	public static String post(String urlStr, String param,String ctype) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = getConnection(url,METHOD_POST,ctype);
			con.setConnectTimeout(DEF_CONN_TIMEOUT);
			
			OutputStream os = con.getOutputStream();
			os.write(param.getBytes(DEF_CHATSET));
			os.flush();
			os.close();
			
			int resultCode= con.getResponseCode();
			if(HttpURLConnection.HTTP_OK==resultCode){
			    
				return getStreamAsString(con.getInputStream(),DEF_CHATSET);
				
			}else{
				throw new DesignException(con.getResponseMessage());
			}
			
		} catch (Exception e) {
			throw new DesignException(e.getMessage());
		}
	}
	
	/**
	 * 环信专用
	 * @param urlStr
	 * @param param
	 * @param ctype
	 * @param authorization
	 * @return
	 */
	public static String post(String urlStr, String param,String ctype,String authorization) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = getConnection(url,METHOD_POST,ctype);
			con.setConnectTimeout(DEF_CONN_TIMEOUT);
			con.setRequestProperty("Authorization", authorization);
			
			OutputStream os = con.getOutputStream();
			os.write(param.getBytes(DEF_CHATSET));
			os.flush();
			os.close();
			
			int resultCode= con.getResponseCode();
			if(HttpURLConnection.HTTP_OK==resultCode){
			    
				return getStreamAsString(con.getInputStream(),DEF_CHATSET);
				
			}else{
				throw new DesignException(con.getResponseMessage());
			}
			
		} catch (Exception e) {
			throw new DesignException(e.getMessage());
		}
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

}

package com.design.service.impl.pay.wechat;

import java.io.Writer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class WxXmlUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml,Class<T> clazz){
		XStream xStream = new XStream();
		xStream.ignoreUnknownElements();
		xStream.processAnnotations(clazz);
		xStream.autodetectAnnotations(true);
		return (T) xStream.fromXML(xml);
	}
	
	public static String toXml(Object obj) {
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
        String xml = xStream.toXML(obj);
        return xml;
	}
}

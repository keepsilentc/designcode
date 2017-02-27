package com.design.service.impl.template;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.google.common.base.Throwables;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Service
public class FreeMarkUtil {
	
	private static Logger log = LoggerFactory.getLogger(FreeMarkUtil.class);
	
	private Configuration cfg;
	
	public Configuration getConfiguration() throws IOException{
		if(cfg==null){
			synchronized (this) {
				if(cfg==null){
					cfg = new Configuration(Configuration.VERSION_2_3_25);
					cfg.setTemplateLoader(new ClassTemplateLoader(FreeMarkUtil.class, "/template"));
					cfg.setDefaultEncoding("UTF-8");
					cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
					cfg.setLogTemplateExceptions(false);
				}
			}
		}
		return cfg;
	}
	
	public String getContent(Object model,String name){
		
		try {
			Configuration config = getConfiguration();
			Template template = config.getTemplate(name);
			StringWriter writer = new StringWriter();
			template.process(model, writer);
			return writer.toString();
		}catch (Exception e) {
			log.info("转换freemarker内容异常,{}",Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
	}
	
	
	
}

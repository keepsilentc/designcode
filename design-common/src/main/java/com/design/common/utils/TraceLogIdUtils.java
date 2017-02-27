package com.design.common.utils;

import java.util.UUID;

import org.slf4j.MDC;

public class TraceLogIdUtils {
	
	public static String traceLogId = "TRACE_LOG_ID";
	
	public static void setTraceLogId(String traceLogId) {
		if(StringUtils.isNotEmpty(traceLogId)){
			MDC.put(TraceLogIdUtils.traceLogId, traceLogId);
		}else{
			MDC.put(TraceLogIdUtils.traceLogId, UUID.randomUUID().toString());
		}
	}
	
	public static String getTraceLogId(){
		return MDC.get(TraceLogIdUtils.traceLogId);
	}
}

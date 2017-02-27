package com.design.common.assist;

import com.design.common.utils.TraceLogIdUtils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TraceLogIdConverter extends ClassicConverter {
	
	@Override
	public String convert(ILoggingEvent event) {
		return event.getMDCPropertyMap().get(TraceLogIdUtils.traceLogId);
	}
}

package com.design.service.impl.padload;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.design.service.api.IPayLoadService;
import com.design.service.api.IPayLoadStrategy;
import com.design.service.api.dto.payload.PayLoad;
@Service
public class PayLoadServiceImpl implements IPayLoadService {
	
	@Resource
	private PayLoadUtil payLoadUtil;
	
	@Override
	public void exec(IPayLoadStrategy strategy) {
		PayLoad payLoad = strategy.getPayLoad();
		payLoadUtil.send(payLoad);
	}
	
	
	
	
	
}

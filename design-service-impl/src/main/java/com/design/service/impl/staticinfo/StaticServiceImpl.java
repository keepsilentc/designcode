package com.design.service.impl.staticinfo;

import org.springframework.stereotype.Service;

import com.design.service.api.IStaticService;
@Service
public class StaticServiceImpl implements IStaticService{

	@Override
	public String getPrivateItems() {
		return "test.url";
	}
	
}

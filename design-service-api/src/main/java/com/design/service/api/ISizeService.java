package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.resp.SizeTypeResp;

public interface ISizeService {

	List<SizeTypeResp> getAllSizeType();
	
}

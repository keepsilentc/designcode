package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.DesignersReq;
import com.design.service.api.dto.resp.DesignerDetailInfoResp;
import com.design.service.api.dto.resp.DesignersResp;

public interface IDesignerService {

	List<DesignersResp> getDesigners(String token);

	List<DesignersResp> searchDesigners(DesignersReq req);
	
	DesignerDetailInfoResp getDesignInfo(String token,Long id);

	void follow(String token, Long designerId,Integer follow);

	

}

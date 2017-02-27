package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.StatusReq;
import com.design.service.api.dto.resp.StatusDetailResp;
import com.design.service.api.dto.resp.StatusListResp;

public interface IStatusService {

	List<StatusListResp> getStatusList(String token,StatusReq req);

	StatusDetailResp getStatusDetail(String token, Long id);

	void markStatus(String token, Long statusId, Integer mark);

	String getShareContent(Long statusId);

}

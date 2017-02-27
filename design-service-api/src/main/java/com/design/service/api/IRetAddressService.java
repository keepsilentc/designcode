package com.design.service.api;

import java.util.List;

import com.design.service.api.dto.req.AddAddressReq;
import com.design.service.api.dto.req.UpdateAddressReq;
import com.design.service.api.dto.resp.RetAddressResp;

public interface IRetAddressService {

	List<RetAddressResp> getAddressList(String token);

	String addAddress(String token, AddAddressReq req);

	void updateAddress(String token, UpdateAddressReq req);
	
	String removeAddress(String token, List<Long> ids);

	void setDefault(String token, Long id);

}

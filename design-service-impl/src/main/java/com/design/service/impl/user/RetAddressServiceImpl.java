package com.design.service.impl.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.dao.entity.RetAddress;
import com.design.dao.entity.dto.RetAddressInfo;
import com.design.dao.persist.RetAddressMapper;
import com.design.service.api.IRetAddressService;
import com.design.service.api.dto.req.AddAddressReq;
import com.design.service.api.dto.req.UpdateAddressReq;
import com.design.service.api.dto.resp.RetAddressResp;
import com.google.common.collect.Maps;
@Service
public class RetAddressServiceImpl implements IRetAddressService{
	
	private static Logger log = LoggerFactory.getLogger(RetAddressServiceImpl.class);
	
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private RetAddressMapper retAddressMapper;
	@Override
	public List<RetAddressResp> getAddressList(String token) {
		//根据token获取用户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		//根据userNo获取用户地址列表
		List<RetAddressInfo> t_addresses = retAddressMapper.getAddressesByUserNo(userNo);
		return CollectionUtils.transfer(t_addresses, RetAddressResp.class, new MyTransfer<RetAddressInfo, RetAddressResp>() {

			@Override
			public void transfer(RetAddressInfo u, RetAddressResp v) {
				v.setAddressId(String.valueOf(u.getId()));
			}
		});
	}
	@Override
	@Transactional
	public String addAddress(String token, AddAddressReq req) {
		//根据token获取用户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		RetAddress address = DozerUtils.transfer(req, RetAddress.class);
		address.setUserNo(userNo);
		address.setIsDefault(0);
		address.setCreateTime(DateUtil.getCurrentDate());
		retAddressMapper.insert(address);
		Integer adderssNum = retAddressMapper.getAddressNumByUserNo(userNo);
		return String.valueOf(adderssNum);
	}
	@Override
	public void updateAddress(String token, UpdateAddressReq req) {
		//根据token获取用户号
		defaultUserServiceImpl.getUserNoByToken(token);
		RetAddress address = DozerUtils.transfer(req, RetAddress.class);
		address.setId(req.getAddressId());
		address.setUpdateTime(DateUtil.getCurrentDate());
		retAddressMapper.updateById(address);
	}
	@Override
	@Transactional
	public String removeAddress(String token, List<Long> ids) {
		//根据token获取用户号
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		if(CollectionUtils.isNotEmpty(ids)){
			retAddressMapper.batchDelete(ids);
		}
		Integer adderssNum = retAddressMapper.getAddressNumByUserNo(userNo);
		return String.valueOf(adderssNum);
	}
	
	@Override
	@Transactional
	public void setDefault(String token, Long id) {
		//校验登陆
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Map<String,Object> param = Maps.newHashMap();
		param.put("id", id);
		param.put("userNo", userNo);
		retAddressMapper.setDefault(param);
	}
	
	public RetAddressInfo getAddressById(String userNo,Long addressId) {
		RetAddressInfo addressInfo = retAddressMapper.getAddressById(userNo,addressId);
		if(addressInfo==null){
			log.info("不存在的地址id");
			throw new DesignException(DesignEx.RETADDRESS_NOT_EXIST);
		}
		return addressInfo;
	}

}

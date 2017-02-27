package com.design.dao.persist;

import com.design.dao.entity.SubscribeMail;

public interface SubscribeMailMapper {
	
	public void insert(SubscribeMail subscribeMail);
	
	public int update(SubscribeMail subscribeMail);
	
	public SubscribeMail getSubscribeInfo(String userNo);
	
}

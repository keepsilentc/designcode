package com.design.service.impl.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.design.common.utils.DateUtil;
import com.design.dao.entity.FeedBack;
import com.design.dao.persist.FeedBackMapper;
import com.design.service.api.IFeedbackService;
import com.design.service.api.dto.req.FeedBackReq;
@Service
public class FeedbackServiceImpl implements IFeedbackService{
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private FeedBackMapper feedBackMapper;
	@Override
	public void feedBack(String token, FeedBackReq req) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		FeedBack feedBack = new FeedBack();
		feedBack.setCreateTime(DateUtil.getCurrentDate());
		feedBack.setDescribed(req.getDescription());
		feedBack.setFeedBackName(req.getName());
		feedBack.setUserNo(userNo);
		feedBack.setIsRead(0);
		feedBackMapper.insert(feedBack);
	}

}

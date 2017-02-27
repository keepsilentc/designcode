package com.design.service.api;

import com.design.service.api.dto.req.FeedBackReq;


public interface IFeedbackService {
	void feedBack(String token, FeedBackReq req);
}

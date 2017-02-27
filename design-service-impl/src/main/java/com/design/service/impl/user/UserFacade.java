package com.design.service.impl.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.enums.UserLevel;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.RedisClient;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.SubscribeMail;
import com.design.dao.entity.User;
import com.design.dao.entity.dto.MarkDesignerInfo;
import com.design.dao.entity.dto.StatusInfo;
import com.design.dao.persist.SubscribeMailMapper;
import com.design.service.api.IUserFacade;
import com.design.service.api.IUserService;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.UpdateUserInfoReq;
import com.design.service.api.dto.ValidateRegisterReq;
import com.design.service.api.dto.resp.DesignerProduct;
import com.design.service.api.dto.resp.MarkDesignerResp;
import com.design.service.api.dto.resp.MarkNumberResp;
import com.design.service.api.dto.resp.MarkStatusResp;
import com.design.service.api.dto.resp.SubScribeMailResp;
import com.design.service.api.dto.resp.UserInfoResp;
import com.design.service.impl.designer.DesignerServiceImpl;
import com.design.service.impl.status.StatusServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UserFacade implements IUserFacade {
	
	private static Logger log = LoggerFactory.getLogger(UserFacade.class);
	
	private Map<String,IUserService> userServices;
	@Resource
	private RedisClient redisClient;
	@Resource
	private StatusServiceImpl statusServiceImpl;
	@Resource
	private DesignerServiceImpl designerServiceImpl;
	@Resource
	private SubscribeMailMapper subscribeMailMapper;
	
	@Override
	public String login(LoginReq loginReq) {
		log.info("登陆服务开始,{}",loginReq);
		IUserService userService = userServices.get(loginReq.getLoginType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		return userService.loginExec(loginReq);
	}
	
	@Override
	public void validatRegisterCode(ValidateRegisterReq validateRegisterReq) {
		log.info("注册验证码校验服务运行,{}",validateRegisterReq);
		IUserService userService = userServices.get(validateRegisterReq.getRegisterType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		userService.validatRegisterCode(validateRegisterReq);
	}
	
	@Override
	public void preRegister(PreRegisterReq preRegisterReq) {
		log.info("注册前服务运行,{}",preRegisterReq);
		IUserService userService = userServices.get(preRegisterReq.getRegisterType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		userService.preRegisterExec(preRegisterReq);
	}
	
	
	@Override
	public void register(RegisterReq registerReq) {
		log.info("注册服务运行,{}",registerReq);
		IUserService userService = userServices.get(registerReq.getRegisterType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		userService.registerExec(registerReq);
	}
	
	@Override
	public void logout(String token) {
		Jedis jedis = redisClient.getResource();
		try {
			String userNo = jedis.get(token);
			if(StringUtils.isNotEmpty(userNo)){
				jedis.del(userNo);
			}
			jedis.del(token);
		}finally{
			redisClient.returnResource(jedis);
		}
	}
	
	@Override
	public void refreshToken(String token) {
		Jedis jedis = redisClient.getResource();
		try{
			String userNo = jedis.get(token);
			if(StringUtils.isEmpty(userNo)){
				throw new DesignException(DesignEx.TOKEN_ERROR);
			}
			jedis.expire(token,Constant.TOKEN_EXPIRETIME);
			jedis.expire(userNo,Constant.TOKEN_EXPIRETIME);
		}finally{
			redisClient.returnResource(jedis);
		}
	}

	public Map<String, IUserService> getUserServices() {
		return userServices;
	}

	public void setUserServices(Map<String, IUserService> userServices) {
		this.userServices = userServices;
	}

	@Override
	public UserInfoResp getUserInfo(String token) {
		log.info("获取用户信息服务运行,{}",token);
		IUserService defaultUserService = userServices.get(Constant.DEFAULT);
		//获取用户信息,如果不存在则抛用户状态异常
		User t_user = defaultUserService.getUserByToken(token);
		UserInfoResp resp = DozerUtils.transfer(t_user, UserInfoResp.class);
		UserLevel level = UserLevel.getLevel(t_user.getLevelId());
		resp.setLevelName(level.getLevelName());
		resp.setLevelIcon(level.getLevelIcon());
		return resp;
	}
	

	@Override
	public void updateUserInfo(String token, UpdateUserInfoReq req) {
		log.info("修改用户信息服务运行,{},{}",token,req);
		IUserService defaultUserService = userServices.get(Constant.DEFAULT);
		User t_user = defaultUserService.getUserByToken(token);
		User u_user = new User();
		u_user.setId(t_user.getId());
		u_user.setNickName(req.getNickName());
		u_user.setRealName(req.getRealName());
		if (req.getGender()!=null) {
			u_user.setGender(req.getGender());
		}
		if(StringUtils.isNotEmpty(req.getBirthDay())){
			u_user.setBirthDay(DateUtil.parse(req.getBirthDay(), DateUtil.PATTERN_1));
		}
		u_user.setUpdateTime(DateUtil.getCurrentDate());
		defaultUserService.updateUserById(u_user);
	}

	@Override
	public MarkNumberResp getMarkNumber(String token) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		Integer statusNum = statusServiceImpl.getMarkStatusNum(userNo);
		Integer designerNum = designerServiceImpl.getMarkDesignerNum(userNo);
		return new MarkNumberResp(String.valueOf(statusNum),String.valueOf(designerNum));
	}
	
	@Override
	public List<MarkDesignerResp> getMarkDesigners(String token) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		List<MarkDesignerInfo> markDesignerInfoList = designerServiceImpl.getMarkDesigners(userNo);
		
		Map<Long,Integer> tempMap = Maps.newHashMap();
		List<MarkDesignerResp> result = Lists.newArrayList();
		Integer index = null;
		MarkDesignerResp tmpMarkDesignerResp = null;
		
		for(MarkDesignerInfo tmp:markDesignerInfoList){
			tmpMarkDesignerResp = DozerUtils.transfer(tmp, MarkDesignerResp.class);
			tmpMarkDesignerResp.setDesignerId(String.valueOf(tmp.getId()));
			if((index=tempMap.get(tmp.getId()))!=null){
				tmpMarkDesignerResp = result.get(index);
			}else{
				tmpMarkDesignerResp.setDesignerProducts(new ArrayList<DesignerProduct>());
				result.add(tmpMarkDesignerResp);
				tempMap.put(tmp.getId(), result.size()-1);
			}
			tmpMarkDesignerResp.getDesignerProducts().add(new DesignerProduct(String.valueOf(tmp.getPicture()),tmp.getProductNo()));
		}
		
		return result;
		
	}

	@Override
	public List<MarkStatusResp> getMarkStatus(String token) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		List<StatusInfo> markStatus =  statusServiceImpl.getMarkStatus(userNo);
		return CollectionUtils.transfer(markStatus, MarkStatusResp.class, new MyTransfer<StatusInfo, MarkStatusResp>() {

			@Override
			public void transfer(StatusInfo u, MarkStatusResp v) {
				if(v != null){
					v.setStatusId(String.valueOf(u.getId()));
				}
			}
		});
	}

	@Override
	public void bindDeviceToken(String token, String deviceToken) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		Assert.notNull(deviceToken, "设备token不能为空");
		IUserService defaultUserService = userServices.get(Constant.DEFAULT);
		//获取用户信息,如果不存在则抛用户状态异常
		User t_user = defaultUserService.getUserByUserNo(userNo);
		t_user.setDeviceToken(deviceToken);
		defaultUserService.updateDeviceTokenUserById(t_user);
	}

	@Override
	public void unbindDeviceToken(String token) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		IUserService defaultUserService = userServices.get(Constant.DEFAULT);
		//获取用户信息,如果不存在则抛用户状态异常
		User t_user = defaultUserService.getUserByUserNo(userNo);
		t_user.setDeviceToken(null);
		defaultUserService.updateDeviceTokenUserById(t_user);
	}

	@Override
	public void subScribeMail(String token, Integer isSubscribe, String subScribeMail) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		SubscribeMail t_subscribeMail = subscribeMailMapper.getSubscribeInfo(userNo);
		if(isSubscribe==1){
			Assert.notNull(subScribeMail, "设备token不能为空");
			if(t_subscribeMail==null){
				t_subscribeMail = new SubscribeMail();
				t_subscribeMail.setUserNo(userNo);
				t_subscribeMail.setIsSubscribe(isSubscribe);
				t_subscribeMail.setSubScribeMail(subScribeMail);
				t_subscribeMail.setCreateTime(DateUtil.getCurrentDate());
				subscribeMailMapper.insert(t_subscribeMail);
			}else{
				t_subscribeMail.setIsSubscribe(1);
				t_subscribeMail.setSubScribeMail(subScribeMail);
				t_subscribeMail.setUpdateTime(DateUtil.getCurrentDate());
				subscribeMailMapper.update(t_subscribeMail);
			}
		}else{
			if(t_subscribeMail==null){
				log.info("用户未订阅,不能取订");
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			}
			t_subscribeMail.setIsSubscribe(0);
			t_subscribeMail.setUpdateTime(DateUtil.getCurrentDate());
			subscribeMailMapper.update(t_subscribeMail);
		}
	}

	@Override
	public SubScribeMailResp getScribeMailInfo(String token) {
		String userNo = userServices.get(Constant.DEFAULT).getUserNoByToken(token);
		SubscribeMail t_subscribeMail = subscribeMailMapper.getSubscribeInfo(userNo);
		SubScribeMailResp result = new SubScribeMailResp();
		if(t_subscribeMail==null){
			result.setIsSubscribe(0);
		}else{
			result.setIsSubscribe(t_subscribeMail.getIsSubscribe());
			result.setSubScribeMail(t_subscribeMail.getSubScribeMail());
		}
		return result;
	}

	@Override
	public void preResetPwd(PreResetPwdReq preResetPwdReq) {
		log.info("重置密码发送验证码服务运行,{}",preResetPwdReq);
		IUserService userService = userServices.get(preResetPwdReq.getRegisterType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		userService.preResetPwdExec(preResetPwdReq);
		
	}

	@Override
	public void resetPwd(ResetPwdReq resetPwdReq) {
		log.info("重置密码服务运行,{}",resetPwdReq);
		IUserService userService = userServices.get(resetPwdReq.getRegisterType());
		if(userService==null)
			throw new DesignException(DesignEx.UNKOWN_TYPE);
		userService.resetPwdExec(resetPwdReq);
	}

}

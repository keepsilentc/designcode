package com.design.service.impl.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.assist.DesignSubmitException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.DateUtil;
import com.design.common.utils.HttpUtil;
import com.design.common.utils.NoGenerator;
import com.design.common.utils.RedisClient;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.User;
import com.design.dao.persist.UserMapper;
import com.design.service.api.IPayLoadService;
import com.design.service.api.IUserService;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.impl.discount.CouponServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class AbstractUserService implements IUserService {
	
	Logger log = LoggerFactory.getLogger(AbstractUserService.class);
	@Resource
	private UserMapper userMapper;
	@Resource
	private RedisClient redisClient;
	@Resource
	private CouponServiceImpl couponServiceImpl;
	@Resource
	private IPayLoadService payLoadServiceImpl;
	
	@Value("${easemob.orgname}")
	private String easemob_OrgName;
	@Value("${easemob.appname}")
	private String easemob_AppName;
	@Value("${easemob.clientid}")
	private String easemob_ClientId;
	@Value("${easemob.clientsecret}")
	private String easemob_ClientSecret;
	
	@Override
	@Transactional(noRollbackFor=DesignSubmitException.class)
	public String loginExec(LoginReq loginReq) {
		User t_user = login(loginReq);
		t_user.setLoginTime(DateUtil.getCurrentDate());
		userMapper.updateUserById(t_user);
		//缓存登陆信息
		String token = cacheLoginInfo(t_user);
		return token;
	}
	
	public abstract User login(LoginReq loginReq);
	
	private String cacheLoginInfo(User t_user) {
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		String userNo = t_user.getUserNo();
		Jedis jedis = redisClient.getResource();
		try{
			String old_token = jedis.get(userNo);
			if(StringUtils.isNotEmpty(old_token)&&jedis.exists(old_token)){
				jedis.del(old_token);
			}
			jedis.set(userNo, token);
			jedis.set(token,t_user.getUserNo());
			jedis.expire(userNo, Constant.TOKEN_EXPIRETIME);
			jedis.expire(token, Constant.TOKEN_EXPIRETIME);
		}finally{
			redisClient.returnResource(jedis);
		}
		return token;
	}
	
	@Override
	public void preRegisterExec(PreRegisterReq preRegisterReq) {
		log.info("注册前服务运行,{}",preRegisterReq);
		preRegister(preRegisterReq);
		
	}
	
	public abstract void preRegister(PreRegisterReq preRegisterReq);
	
	@Override
	public void registerExec(RegisterReq registerReq) {
		log.info("注册服务运行,{}",registerReq);
		//注册尺寸
		User t_user = register(registerReq);
		//注册环信
		registerEasemob(t_user);
		
		//发送新用户优惠券
		try{
			couponServiceImpl.pushCoupon(t_user.getUserNo(),"KOcw2017021619122944112");
			//感谢您注册CHICUN，已自动为您领取9折优惠券！
			//推送
		}catch(Exception e){
			log.info("发送新用户优惠券失败,{}",Throwables.getStackTraceAsString(e));
		}
	}

	public void registerEasemob(User t_user) {
		StringBuilder urlBuilder = new StringBuilder("http://a1.easemob.com/");
		urlBuilder.append(easemob_OrgName);
		urlBuilder.append("/");
		urlBuilder.append(easemob_AppName);
		String token = null;
		{
			int i = 1;
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("grant_type", "client_credentials");
			jsonParam.put("client_id", easemob_ClientId);
			jsonParam.put("client_secret", easemob_ClientSecret);
			while(i<4){
				try{
					String result = HttpUtil.post(urlBuilder.toString()+"/token",jsonParam.toJSONString(), "application/json");
					JSONObject tokenResp = JSONObject.parseObject(result);
					token = tokenResp.getString("access_token");
					break;
				}catch(Exception e){
					log.info("获取环信token失败,第{}次",i);
				}finally{
					i++;
				}
			}
		}
		
		{
			int i = 1;
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("username", t_user.getUserNo().toLowerCase());
			jsonParam.put("password", "123456");
			jsonParam.put("nickname", t_user.getNickName());
			while(i<4){
				try{
					String result = HttpUtil.post(urlBuilder.toString()+"/users",jsonParam.toJSONString(),"application/json","Bearer "+token);
					log.info("环信注册返回：{}",result);
					break;
				}catch(Exception e){
					log.info("注册环信失败,第{}次",i);
				}finally{
					i++;
				}
			}
		}
	}
	
	public abstract User register(RegisterReq registerReq);

	public void initRegisterUser(User user) {
		//初始为1级
		user.setLevelId(Constant.LEVEL_ID_INIT);
		user.setUserNo(NoGenerator.generateUserNo());
		user.setUserName("DEFAULT");
		user.setGender(Constant.MALE);
		user.setAge(0);
		user.setMistakeNum(0);
		user.setIsEnable(Constant.ENABLE);
		user.setCreateTime(DateUtil.getCurrentDate());
		if(StringUtils.isEmpty(user.getNickName())){
			Jedis jedis = redisClient.getResource();
			try {
				if(jedis.llen("nickname")<50){
					BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("nickname.txt")));
					List<String> names = Lists.newArrayList();
					String temp = null;
					
						while((temp=reader.readLine())!=null){
							names.add(temp);
						}
					Random random = new Random();
					while(jedis.llen("nickname")<500){
						int m = random.nextInt(names.size());
						int n = random.nextInt(names.size());
						jedis.rpush("nickname",names.get(m)+names.get(n));
					}
				}
				user.setNickName(jedis.lpop("nickname"));
			} catch (IOException e) {
				throw new DesignException(e.getMessage());
			}finally{
				redisClient.returnResource(jedis);
			}
		}
	}
	
	@Override
	public User getUserByCondition(Map<String, Object> params) {
		User t_user = userMapper.getUserByCondition(params);
		if(t_user==null){
			throw new DesignException(DesignEx.USER_STATUS_ERROR);
		}
		return t_user;
	}
	
	/**
	 * 根据token获取用户信息
	 */
	@Override
	public String getUserNoByToken(String token){
		Jedis jedis = redisClient.getResource();
		try {
			String userNo = jedis.get(token);
			if(StringUtils.isEmpty(userNo)){
				throw new DesignException(DesignEx.TOKEN_ERROR);
			}
			return userNo;
		}finally{
			redisClient.returnResource(jedis);
		}
	}
	
	
	/**
	 * 根据token获取用户信息
	 */
	@Override
	public User getUserByToken(String token){
		Jedis jedis = redisClient.getResource();
		try {
			String userNo = jedis.get(token);
			if(StringUtils.isEmpty(userNo)){
				throw new DesignException(DesignEx.TOKEN_ERROR);
			}
			Map<String,Object> params = Maps.newHashMap();
			params.put("userNo", userNo);
			//获取用户信息,如果不存在则抛用户状态异常
			return getUserByCondition(params);
		}finally{
			redisClient.returnResource(jedis);
		}
	}
	
	/**
	 * 更新用户信息,此方法无法修改userNo,mobileNo,email
	 */
	@Override
	public void updateUserById(User t_user) {
		userMapper.updateUserById(t_user);
	}

	@Override
	public User getUserByUserNo(String userNo) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo", userNo);
		//获取用户信息,如果不存在则抛用户状态异常
		return getUserByCondition(params);
	}

	@Override
	public void updateDeviceTokenUserById(User t_user) {
		userMapper.updateDeviceTokenUserById(t_user);
	}
	
}

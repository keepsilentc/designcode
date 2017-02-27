package com.design.service.impl.user;

import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.assist.DesignSubmitException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.Md5Util;
import com.design.common.utils.RedisClient;
import com.design.common.utils.StringUtils;
import com.design.common.utils.ValidateUtils;
import com.design.dao.entity.User;
import com.design.dao.persist.UserMapper;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.Mobiles;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.ValidateRegisterReq;
import com.design.service.impl.padload.SendSms;
import com.google.common.collect.Maps;
@Service
public class MobileUserServiceImpl extends AbstractUserService{
	
	private static Logger log = LoggerFactory.getLogger(MobileUserServiceImpl.class);
	
	@Resource
	private RedisClient redisClient;
	@Resource
	private UserMapper userMapper;
	@Resource
	private SendSms sendSms;
	
	@Override
	public User login(LoginReq loginReq) {
		ValidateUtils.validateEntiryThrows(loginReq, Mobiles.class);
		Map<String,Object> params = Maps.newHashMap();
		params.put("isEnable", Constant.ENABLE);
		User t_user = null;
		params.put("mobileNo", loginReq.getMobileNo());
		t_user = userMapper.getUserByCondition(params);
		if(t_user==null)
		{
			log.info("登陆失败,用户不存在");
			throw new DesignException(DesignEx.UNREGISTERED);
		}else{
			if(!t_user.getPasswd().equals(Md5Util.string2MD5(loginReq.getPasswd()))){
				log.info("登陆失败,密码错误");
				t_user.setMistakeNum(t_user.getMistakeNum()+1);
				userMapper.updateUserById(t_user);
				throw new DesignSubmitException(DesignEx.USERPASSWORDERROR);
			}
		}
		return t_user;
	}
	
	@Override
	public void validatRegisterCode(ValidateRegisterReq validateRegisterReq) {
		//注册验证码
		Jedis jedis = redisClient.getResource();
		try {
			String registerCode = jedis.hget(validateRegisterReq.getMobileNo(), Constant.REGISTERCODE);
			if(StringUtils.isEmpty(registerCode)||!registerCode.equals(validateRegisterReq.getRegisterCode())){
				throw new DesignException(DesignEx.REGISTERCODE_ERROR);
			}
		}finally{
			redisClient.returnResource(jedis);
		}
	}

	
	@Override
	public void preRegister(PreRegisterReq preRegisterReq) {
		ValidateUtils.validateEntiryThrows(preRegisterReq, Mobiles.class);
		Map<String,String> map = Maps.newHashMap();
		map.put("mobileNo", preRegisterReq.getMobileNo());
		if(userMapper.isUserExist(map)>0){
			log.info("注册异常,已经存在该用户");
			throw new DesignException(DesignEx.MOBILE_ALREADY_REGISTERED);
		}
		//缓存注册验证码
		cacheRegisterCode(preRegisterReq);
	}
	
	private void cacheRegisterCode(PreRegisterReq preRegisterReq) {
		Jedis jedis = redisClient.getResource();
		try {
			String mobileNo = preRegisterReq.getMobileNo();
			Random random = new Random();
			int registerCode = 1000 + random.nextInt(8999);
//			if (jedis.hget(mobileNo, Constant.REGISTERCODE) != null)
//				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			jedis.hset(mobileNo, Constant.REGISTERCODE,String.valueOf(registerCode));
			jedis.expire(mobileNo, Constant.REGISTEREXPIRE);
//			【CHICUN】xxxxxx（手机注册验证码），请在5分钟内完成注册。如非本人操作，请您尽快联系客服处理。
			StringBuilder builder = new StringBuilder();
			builder.append(registerCode);
			builder.append("（手机注册验证码），请在5分钟内完成注册。如非本人操作，请您尽快联系客服处理。");
			sendSms.sendSms(builder.toString(),mobileNo);
		} finally{
			redisClient.returnResource(jedis);
		}
		
	}

	@Override
	public User register(RegisterReq registerReq) {
		ValidateUtils.validateEntiryThrows(registerReq, Mobiles.class);
		Jedis jedis = redisClient.getResource();
		try {
			//注册验证码
			String registerCode = jedis.hget(registerReq.getMobileNo(), Constant.REGISTERCODE);
			if(StringUtils.isEmpty(registerCode)||!registerCode.equals(registerReq.getRegisterCode())){
				throw new DesignException(DesignEx.REGISTERCODE_ERROR);
			}
			Map<String,String> params = Maps.newHashMap();
			params.put("mobileNo", registerReq.getMobileNo());
			if(userMapper.isUserExist(params)>0){
				log.info("注册异常,已经存在该用户");
				throw new DesignException(DesignEx.MOBILE_ALREADY_REGISTERED);
			}
			User user = new User();
			//注册设置默认的用户字段
			initRegisterUser(user);
			user.setMobileNo(registerReq.getMobileNo());
			user.setPasswd(Md5Util.string2MD5(registerReq.getPasswd()));
			//同步校验
			synchronized (this) {
				if(userMapper.isUserExist(params)>0){
					log.info("注册并发异常,已经存在该用户");
					throw new DesignException(DesignEx.CONCURRENCY_ERROR);
				}else{
					userMapper.insert(user);
					jedis.hdel(registerReq.getMobileNo(), Constant.REGISTERCODE);
				}
			}
			return user;
		}finally{
			redisClient.returnResource(jedis);
		}
		
	}
	
	@Override
	public void preResetPwdExec(PreResetPwdReq preResetPwdReq) {
		ValidateUtils.validateEntiryThrows(preResetPwdReq, Mobiles.class);
		Map<String,Object> params = Maps.newHashMap();
		params.put("mobileNo", preResetPwdReq.getMobileNo());
		if(userMapper.getUserByCondition(params)==null){
			throw new DesignException(DesignEx.USER_NOT_FIND);
		}
		//缓存注册验证码
		cacheResetPwdCode(preResetPwdReq);
		//发送邮件
		//待实现
	}

	private String cacheResetPwdCode(PreResetPwdReq preResetPwdReq) {
		Jedis jedis = redisClient.getResource();
		try {
			Random random = new Random();
			int resetPwdCode = 1000 + random.nextInt(8999);
			String mobileNo = preResetPwdReq.getMobileNo();
			if (jedis.hget(mobileNo, Constant.RESETPWDCODE) != null)
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			jedis.hset(mobileNo, Constant.RESETPWDCODE,String.valueOf(resetPwdCode));
			jedis.expire(mobileNo, Constant.REGISTEREXPIRE);
			//【CHICUN】xxxxxx（密码找回验证码），请在5分钟内修改密码。如非本人操作，请您尽快联系客服处理。
			StringBuilder builder = new StringBuilder();
			builder.append(resetPwdCode);
			builder.append("（密码找回验证码），请在5分钟内修改密码。如非本人操作，请您尽快联系客服处理。");
			sendSms.sendSms(builder.toString(),mobileNo);
			return String.valueOf(resetPwdCode);
		}finally{
			redisClient.returnResource(jedis);
		}
	}

	@Override
	public void resetPwdExec(ResetPwdReq resetPwdReq) {
		ValidateUtils.validateEntiryThrows(resetPwdReq, Mobiles.class);
		Jedis jedis = redisClient.getResource();
		try {
			//重置密码验证码
			String resetPwdCode = jedis.hget(resetPwdReq.getMobileNo(), Constant.RESETPWDCODE);
			if(StringUtils.isEmpty(resetPwdCode)||!resetPwdCode.equals(resetPwdReq.getResetPwdCode())){
				throw new DesignException(DesignEx.RESETPWDCODE_ERROR);
			}
			Map<String,Object> params = Maps.newHashMap();
			params.put("mobileNo", resetPwdReq.getMobileNo());
			
			User t_user = userMapper.getUserByCondition(params);
			if(t_user==null){
				throw new DesignException(DesignEx.USER_NOT_FIND);
			}
			t_user.setPasswd(Md5Util.string2MD5(resetPwdReq.getPasswd()));
			userMapper.updatePwd(t_user);
			jedis.hdel(resetPwdReq.getMobileNo(), Constant.RESETPWDCODE);
		}finally{
			redisClient.returnResource(jedis);
		}
	}
}

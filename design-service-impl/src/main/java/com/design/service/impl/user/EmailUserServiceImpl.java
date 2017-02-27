package com.design.service.impl.user;

import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.hibernate.validator.constraints.Email;
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
import com.design.service.api.dto.Emails;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.ValidateRegisterReq;
import com.design.service.impl.padload.MailUtil;
import com.design.service.impl.template.FreeMarkUtil;
import com.google.common.collect.Maps;
@Service
public class EmailUserServiceImpl extends AbstractUserService{
	
	private static Logger log = LoggerFactory.getLogger(EmailUserServiceImpl.class);
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private MailUtil mailUtil;
	@Resource
	private FreeMarkUtil freeMarkUtil;
	@Resource
	private RedisClient redisClient;
	@Override
	public User login(LoginReq loginReq) {
		ValidateUtils.validateEntiryThrows(loginReq, Emails.class);
		Map<String,Object> params = Maps.newHashMap();
		params.put("isEnable", Constant.ENABLE);
		User t_user = null;
		params.put("email", loginReq.getEmail());
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
		Jedis jedis = redisClient.getResource();
		try {
			//注册验证码
			String registerCode = jedis.hget(validateRegisterReq.getEmail(), Constant.REGISTERCODE);
			if(StringUtils.isEmpty(registerCode)||!registerCode.equals(validateRegisterReq.getRegisterCode())){
				throw new DesignException(DesignEx.REGISTERCODE_ERROR);
			}
		}finally{
			redisClient.returnResource(jedis);
		}
		
	}
	
	@Override
	public void preRegister(PreRegisterReq preRegisterReq) {
		ValidateUtils.validateEntiryThrows(preRegisterReq, Emails.class);
		Map<String,String> map = Maps.newHashMap();
		map.put("email", preRegisterReq.getEmail());
		if(userMapper.isUserExist(map)>0){
			log.info("注册异常,已经存在该用户");
			throw new DesignException(DesignEx.EMAILALREADY_REGISTERED);
		}
		//缓存注册验证码
		cacheRegisterCode(preRegisterReq);
		//发送邮件
		//待实现
	}
	
	private String cacheRegisterCode(PreRegisterReq preRegisterReq) {
		Jedis jedis = redisClient.getResource();
		try {
			Random random = new Random();
			int registerCode = 1000 + random.nextInt(8999);
			if (jedis.hget(preRegisterReq.getEmail(), Constant.REGISTERCODE) != null)
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			jedis.hset(preRegisterReq.getEmail(), Constant.REGISTERCODE,String.valueOf(registerCode));
			jedis.expire(preRegisterReq.getEmail(), Constant.REGISTEREXPIRE);
			return String.valueOf(registerCode);
		}finally{
			redisClient.returnResource(jedis);
		}
	}

	@Override
	public User register(RegisterReq registerReq) {
		ValidateUtils.validateEntiryThrows(registerReq, Email.class);
		Jedis jedis = redisClient.getResource();
		try {
			/*//注册验证码
			String registerCode = jedis.hget(registerReq.getEmail(), Constant.REGISTERCODE);
			if(StringUtils.isEmpty(registerCode)||!registerCode.equals(registerReq.getRegisterCode())){
				throw new DesignException(DesignEx.REGISTERCODE_ERROR);
			}*/
			Map<String,String> map = Maps.newHashMap();
			map.put("email", registerReq.getEmail());
			if(userMapper.isUserExist(map)>0){
				log.info("注册异常,已经存在该用户");
				throw new DesignException(DesignEx.EMAILALREADY_REGISTERED);
			}
			User user = new User();
			//注册设置默认的用户字段
			initRegisterUser(user);
			user.setEmail(registerReq.getEmail());
			user.setPasswd(Md5Util.string2MD5(registerReq.getPasswd()));
			//同步校验
			synchronized (this) {
				if(userMapper.isUserExist(map)>0){
					log.info("注册并发异常,已经存在该用户");
					throw new DesignException(DesignEx.CONCURRENCY_ERROR);
				}else{
					userMapper.insert(user);
					jedis.hdel(registerReq.getEmail(), Constant.REGISTERCODE);
				}
			}
			return user;
		}finally{
			redisClient.returnResource(jedis);
		}
		
	}

	@Override
	public void preResetPwdExec(PreResetPwdReq preResetPwdReq) {
		ValidateUtils.validateEntiryThrows(preResetPwdReq, Emails.class);
		Map<String,Object> params = Maps.newHashMap();
		params.put("email", preResetPwdReq.getEmail());
		User t_user = userMapper.getUserByCondition(params);
		if(t_user==null){
			throw new DesignException(DesignEx.USER_NOT_FIND);
		}
		//缓存注册验证码
		cacheResetPwdCode(preResetPwdReq,t_user);
		//发送邮件
		//待实现
	}

	private String cacheResetPwdCode(PreResetPwdReq preResetPwdReq,User t_user) {
		Jedis jedis = redisClient.getResource();
		try {
			Random random = new Random();
			int resetPwdCode = 1000 + random.nextInt(8999);
//			if (jedis.hget(preResetPwdReq.getEmail(), Constant.RESETPWDCODE) != null)
//				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			jedis.hset(preResetPwdReq.getEmail(), Constant.RESETPWDCODE,String.valueOf(resetPwdCode));
			jedis.expire(preResetPwdReq.getEmail(), Constant.REGISTEREXPIRE);
			
			Map<String,Object> param = Maps.newHashMap();
			param.put("nickname", t_user.getNickName());
			param.put("resetPwdCode", resetPwdCode);
			
			String content = freeMarkUtil.getContent(param, "resetPwd.ftl");
			mailUtil.transport("CHICUN尺寸—密码找回验证邮件", content, preResetPwdReq.getEmail());
			return String.valueOf(resetPwdCode);
		}finally{
			redisClient.returnResource(jedis);
		}
	}

	@Override
	public void resetPwdExec(ResetPwdReq resetPwdReq) {
		ValidateUtils.validateEntiryThrows(resetPwdReq, Email.class);
		Jedis jedis = redisClient.getResource();
		try {
			//重置密码验证码
			String resetPwdCode = jedis.hget(resetPwdReq.getEmail(), Constant.RESETPWDCODE);
			if(StringUtils.isEmpty(resetPwdCode)||!resetPwdCode.equals(resetPwdReq.getResetPwdCode())){
				throw new DesignException(DesignEx.RESETPWDCODE_ERROR);
			}
			Map<String,Object> params = Maps.newHashMap();
			params.put("email", resetPwdReq.getEmail());
			
			User t_user = userMapper.getUserByCondition(params);
			if(t_user==null){
				throw new DesignException(DesignEx.USER_NOT_FIND);
			}
			t_user.setPasswd(Md5Util.string2MD5(resetPwdReq.getPasswd()));
			userMapper.updatePwd(t_user);
			jedis.hdel(resetPwdReq.getEmail(), Constant.RESETPWDCODE);
		}finally{
			redisClient.returnResource(jedis);
		}
	}

}

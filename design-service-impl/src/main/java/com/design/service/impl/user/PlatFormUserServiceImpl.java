package com.design.service.impl.user;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.utils.StringUtils;
import com.design.common.utils.ValidateUtils;
import com.design.dao.entity.User;
import com.design.dao.persist.UserMapper;
import com.design.service.api.dto.LoginReq;
import com.design.service.api.dto.PlatForm;
import com.design.service.api.dto.PreRegisterReq;
import com.design.service.api.dto.PreResetPwdReq;
import com.design.service.api.dto.RegisterReq;
import com.design.service.api.dto.ResetPwdReq;
import com.design.service.api.dto.ValidateRegisterReq;
import com.design.service.impl.discount.CouponServiceImpl;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
@Service
public class PlatFormUserServiceImpl extends AbstractUserService{
	
	private static Logger log = LoggerFactory.getLogger(PlatFormUserServiceImpl.class);
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private CouponServiceImpl couponServiceImpl;
	
	@Override
	public User login(LoginReq loginReq) {
		ValidateUtils.validateEntiryThrows(loginReq, PlatForm.class);
		Map<String,Object> params = Maps.newHashMap();
		params.put("platformId", loginReq.getUserId());
		params.put("platformType", loginReq.getPlatform());
		User t_user = userMapper.getUserByCondition(params);
		if(t_user==null){
			log.info("第三方用户在用户表中不存在,开始插入");
			t_user = new User();
			//初始化用户表数据
			t_user.setPlatformId(loginReq.getUserId());
			t_user.setPlatformType(loginReq.getPlatform());
			String nickName = loginReq.getNickName();
			if(StringUtils.isNotEmpty(loginReq.getNickName())){
				nickName = nickName.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", ""); //过滤emoji表情
			}
			t_user.setNickName(nickName);
			initRegisterUser(t_user);
			userMapper.insert(t_user);
			//注册环信
			registerEasemob(t_user);
			//发送新用户优惠券
			try{
				couponServiceImpl.pushCoupon(t_user.getUserNo(),"KOcw2017021619122944112");
			}catch(Exception e){
				log.info("发送新用户优惠券失败,{}",Throwables.getStackTraceAsString(e));
			}
		}
		return t_user;
	}
	
	@Override
	public void preRegister(PreRegisterReq preRegisterReq) {
		throw new UnsupportedOperationException("第三方类型不支持注册");
	}

	@Override
	public User register(RegisterReq registerReq) {
		throw new UnsupportedOperationException("第三方类型不支持注册");
	}

	@Override
	public void validatRegisterCode(ValidateRegisterReq validateRegisterReq) {
		throw new UnsupportedOperationException("第三方类型不支持注册");
	}

	@Override
	public void preResetPwdExec(PreResetPwdReq preResetPwdReq) {
		throw new UnsupportedOperationException("第三方类型不支持重置密码");
	}

	@Override
	public void resetPwdExec(ResetPwdReq resetPwdReq) {
		throw new UnsupportedOperationException("第三方类型不支持重置密码");
	}
	

}

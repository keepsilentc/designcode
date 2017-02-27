package com.design.service.impl.discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.CouponState;
import com.design.common.enums.CouponStrategy;
import com.design.common.enums.CouponType;
import com.design.common.enums.DesignEx;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.Coupon;
import com.design.dao.entity.CouponUser;
import com.design.dao.entity.dto.CouponInfo;
import com.design.dao.persist.CouponMapper;
import com.design.dao.persist.CouponUserMapper;
import com.design.service.api.ICouponService;
import com.design.service.api.IUserService;
import com.design.service.api.dto.resp.CouponResp;

public class CouponServiceImpl implements ICouponService{
	
	private static Logger log = LoggerFactory.getLogger(CouponServiceImpl.class);
	
	private Map<String,ICouponStrategy> couponStrategyServices;
	
	@Resource
	private IUserService defaultUserServiceImpl;
	@Resource
	private CouponMapper couponMapper;
	@Resource
	private CouponUserMapper couponUserMapper;
	
	@Override
	public BigDecimal disountMoney(String userNo,String couponNo,BigDecimal amount){
		if(StringUtils.isEmpty(couponNo)){
			return new ChicunMoney().getMoney();
		}
		
		Coupon t_coupon = getValidCoupon(couponNo);
		getValidCouponUser(userNo,couponNo);
		
		CouponStrategy couponStrategy = CouponStrategy.get(t_coupon.getCouponStrategy());
		if(couponStrategy==null){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		ICouponStrategy couponStrategyService = couponStrategyServices.get(couponStrategy.name());
		if(couponStrategyService==null){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		return couponStrategyService.doStrategy(t_coupon,amount);
	}
	
	public Coupon getValidCoupon(String couponNo){
		Coupon t_coupon = getCoupon(couponNo);
		if(Constant.UNENABLE.equals(t_coupon.getIsEnable())){
			log.info("优惠券未启用,{}",couponNo);
			throw new DesignException(DesignEx.COUPON_NOTENABLE);
		}
		if(DateUtil.isBefore(t_coupon.getStartTime())){
			log.info("优惠券时间未开始,{}",couponNo);
			throw new DesignException(DesignEx.COUPON_NOTSTART);
		}
		if(DateUtil.isAfter(t_coupon.getEndTime())){
			log.info("优惠券已过期,{}",couponNo);
			throw new DesignException(DesignEx.COUPON_STOPPED);
		}
		return t_coupon;
	}
	
	public Coupon getCoupon(String couponNo){
		Coupon t_coupon = couponMapper.getCoupon(couponNo);
		if(t_coupon==null){
			log.info("优惠券不存在,{}",couponNo);
			throw new DesignException(DesignEx.COUPON_NOTEXIST);
		}
		return t_coupon;
	}

	
	@Override
	public List<CouponResp> getUserCouponList(String token,String validFlag) {
		final String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		List<Coupon> coupons = couponMapper.getCouponListByUserNo(userNo,validFlag);
		
		return CollectionUtils.transfer(coupons, CouponResp.class, new MyTransfer<Coupon, CouponResp>() {

			@Override
			public void transfer(Coupon from, CouponResp to) {
				StringBuilder builder = new StringBuilder();
				if(CouponStrategy.DISCOUNT.getStrategyCode().equals(from.getCouponStrategy())){
					if(!ChicunMoney.isInteger(from.getCouponRate())){
						builder.append(new ChicunMoney(from.getCouponRate()).multiply(10).setScale(0));
					}else{
						builder.append(from.getCouponRate().setScale(0));
					}
					builder.append("折优惠");
					to.setSalesPromote(builder.toString());
				}
				if(CouponStrategy.FULL_REDUCTION.getStrategyCode().equals(from.getCouponStrategy())){
					builder.append("满");
					builder.append(from.getFullMoney());
					builder.append("减");
					builder.append(from.getMinusMoney());
					to.setSalesPromote(builder.toString());
				}
			}
			
		});
	}
	
	public CouponUser getValidCouponUser(String userNo,String couponNo){
		CouponUser t_couponUser = couponUserMapper.getCouponUser(userNo,couponNo);
		if(t_couponUser==null){
			log.info("用户优惠券表无相应记录,userNo:{},couponNo:{}",userNo,couponNo);
			throw new DesignException(DesignEx.COUPON_NOTEXIST);
		}
		if(!CouponState.UNUSE.getStateCode().equals(t_couponUser.getUserCouponState())){
			log.info("用户优惠券表不能使用,userNo:{},couponNo:{}",userNo,couponNo);
			throw new DesignException(DesignEx.COUPON_NOTEXIST);
		}
		return t_couponUser;
	}
	
	@Override
	public void lockCoupon(String userNo,String couponNo) {
		int i = Constant.TRYTIMES;
		do{
			i--;
			CouponUser t_couponUser = getValidCouponUser(userNo,couponNo);
			t_couponUser.setUserCouponState(CouponState.USED.getStateCode());
			t_couponUser.setUseTime(DateUtil.getCurrentDate());
			int num = couponUserMapper.update(t_couponUser);
			if(num==1){
				return ;
			}
		}while(i>0);
		log.info("使用优惠券异常");
		throw new DesignException(DesignEx.CONCURRENCY_ERROR);
	}
	
	public boolean judgeCouponCanUse(Coupon coupon){
		boolean flag = true;
		flag=flag&&!DateUtil.isBefore(coupon.getStartTime())&&!DateUtil.isAfter(coupon.getEndTime());
		return flag;
	}

	public Map<String, ICouponStrategy> getCouponStrategyServices() {
		return couponStrategyServices;
	}


	public void setCouponStrategyServices(
			Map<String, ICouponStrategy> couponStrategyServices) {
		this.couponStrategyServices = couponStrategyServices;
	}

	@Override
	public BigDecimal getDisountMoney(String userNo, String couponNo,BigDecimal amount) {
		if(StringUtils.isEmpty(couponNo)){
			return new ChicunMoney().getMoney();
		}
		
		Coupon t_coupon = getCoupon(couponNo);
		
		CouponStrategy couponStrategy = CouponStrategy.get(t_coupon.getCouponStrategy());
		if(couponStrategy==null){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		ICouponStrategy couponStrategyService = couponStrategyServices.get(couponStrategy.name());
		if(couponStrategyService==null){
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		return couponStrategyService.doStrategy(t_coupon,amount);
	}

	public void pushCoupon(String userNo, String couponNo) {
		getCoupon(couponNo);
		insertCouponUser(couponNo, userNo);
	}

	@Override
	@Transactional
	public void receiveCoupon(String token, String couponNo) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Coupon t_coupon = getValidCoupon(couponNo);
		
		if(CouponType.UNLIMIT.getTypeCode().equals(t_coupon.getCouponType())){
			//若超过总数限制，不允许领取
			Integer receiveQuantity = couponUserMapper.getCouponReceivedQuantity(couponNo);
			if(receiveQuantity.compareTo(t_coupon.getSumNum())!=-1){
				log.info("优惠券已被领完");
				throw new DesignException(DesignEx.COUPON_ALL_RECEIVED);
			}
			//若用户存在未使用的此优惠券，不允许领取
			CouponUser t_CouponUser = couponUserMapper.getCouponUser(userNo, couponNo);
			if(t_CouponUser!=null&&CouponState.UNUSE.getStateCode().equals(t_CouponUser.getUserCouponState())){
				log.info("该用户存在未使用的此优惠券,{},{}",userNo, couponNo);
				throw new DesignException(DesignEx.COUPON_NOTUSED_RECEIVE);
			}
			
			insertCouponUser(couponNo, userNo);
		}else if(CouponType.SINGLEUSER.getTypeCode().equals(t_coupon.getCouponType())){
			//单用户使用一次的券，单用户只能拥有一张
			if(couponUserMapper.getCouponUserQuantity(userNo,couponNo)!=0){
				throw new DesignException(DesignEx.COUPON_REPEAT_RECEIVE);
			}
			
			insertCouponUser(couponNo, userNo);
			
			if(couponUserMapper.getCouponUserQuantity(userNo,couponNo)>1){
				log.info("并发异常");
				throw new DesignException(DesignEx.CONCURRENCY_ERROR);
			}
		}
	}

	private CouponUser insertCouponUser(String couponNo, String userNo) {
		CouponUser couponUser = new CouponUser();
		couponUser.setCouponNo(couponNo);
		couponUser.setUserNo(userNo);
		couponUser.setIsEnable(Constant.ENABLE);
		couponUser.setModifyCount(0);
		couponUser.setReceiveTime(DateUtil.getCurrentDate());
		couponUser.setUserCouponState(CouponState.UNUSE.getStateCode());
		couponUserMapper.insert(couponUser);
		return couponUser;
	}

	@Override
	public List<CouponResp> searchCoupon(String couponName) {
		List<CouponInfo> t_couponInfos = couponMapper.getCouponListByCouponName(couponName);
		return CollectionUtils.transfer(t_couponInfos, CouponResp.class, new MyTransfer<CouponInfo, CouponResp>() {

			@Override
			public void transfer(CouponInfo from, CouponResp to) {
				StringBuilder builder = new StringBuilder();
				if(CouponStrategy.DISCOUNT.getStrategyCode().equals(from.getCouponStrategy())){
					if(!ChicunMoney.isInteger(from.getCouponRate())){
						builder.append(new ChicunMoney(from.getCouponRate()).multiply(10).setScale(0));
					}else{
						builder.append(from.getCouponRate().setScale(0));
					}
					builder.append("折优惠");
					to.setSalesPromote(builder.toString());
				}
				if(CouponStrategy.FULL_REDUCTION.getStrategyCode().equals(from.getCouponStrategy())){
					builder.append("满");
					builder.append(from.getFullMoney());
					builder.append("减");
					builder.append(from.getMinusMoney());
					to.setSalesPromote(builder.toString());
				}
				if(judgeCouponCanUse(from)){
					to.setValid("1");
				}else{
					to.setValid("0");
				}
			}
		});
	}

}

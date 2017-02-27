package com.design.service.impl.common;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.ChicunMoney;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.RedisClient;
import com.design.common.utils.SerializationUtil;
import com.design.dao.entity.Carousel;
import com.design.dao.entity.Country;
import com.design.dao.entity.PayType;
import com.design.dao.entity.dto.PcrInfo;
import com.design.dao.persist.AreaMapper;
import com.design.dao.persist.CarouselMapper;
import com.design.dao.persist.PayTypeMapper;
import com.design.service.api.ICommonService;
import com.design.service.api.dto.resp.CarouselResp;
import com.design.service.api.dto.resp.CityResp;
import com.design.service.api.dto.resp.CountryResp;
import com.design.service.api.dto.resp.PayTypeResp;
import com.design.service.api.dto.resp.PcrResp;
import com.design.service.api.dto.resp.RegionResp;
import com.google.common.collect.Lists;

@Service
public class CommonServiceImpl implements ICommonService {
	
	private static Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	@Resource
	private RedisClient redisClient;
	
	@Resource
	private AreaMapper areaMapper;

	@Resource
	private PayTypeMapper payTypeMapper;
	
	@Resource
	private CarouselMapper carouselMapper;
	
	@Override
	public List<CarouselResp> getCarouselList() {
		List<Carousel> carouselList = Lists.newArrayList();
		String key = redisClient.generateKey(CarouselMapper.class,"getCarouselList");
		Jedis jedis = redisClient.getResource();
		try{
			if (jedis.llen(key) != 0) {
				List<byte[]> tmps = jedis.lrange(key.getBytes(),0,-1);
				Carousel carousel = null;
				for(byte[] tmp:tmps){
					carousel = SerializationUtil.deserialize(tmp);
					carouselList.add(carousel);
				}
			}else{
				carouselList = carouselMapper.getCarouselList();
				for(Carousel carousel:carouselList){
					jedis.rpush(key.getBytes(),SerializationUtil.serialize(carousel));
				}
				jedis.expire(key.getBytes(), Constant.KEY_EXPIRETIME);
			}
		}finally{
			redisClient.returnResource(jedis);
		}
		return DozerUtils.transferList(carouselList, CarouselResp.class);
	}
	
	
	@Override
	public List<PayTypeResp> getPayList() {
		List<PayType> payTypeList = getPayTypes();
		List<PayTypeResp> payTypeResps = Lists.newArrayList();
		PayTypeResp temp_resp = null;
		for(PayType tmp:payTypeList){
			temp_resp = new PayTypeResp();
			temp_resp.setPayId(tmp.getId());
			temp_resp.setPayName(tmp.getName());
			temp_resp.setPayIcon(String.valueOf(tmp.getPayIcon()));
			payTypeResps.add(temp_resp);
		}
		return payTypeResps;
	}
	
	public PayType getPayType(String payId) {
		List<PayType> payTypList = getPayTypes();
		for(PayType tmp:payTypList){
			if(tmp.getId().equals(payId)){
				return tmp;
			}
		}
		throw new DesignException(DesignEx.INTERNAL_ERROR);
	}

	public List<PayType> getPayTypes() {
		List<PayType> payTypeList = Lists.newArrayList();
		String key = redisClient.generateKey(PayTypeMapper.class,"getPaylist");
		Jedis jedis = redisClient.getResource();
		try{
			if (jedis.llen(key) != 0) {
				List<byte[]> tmps = jedis.lrange(key.getBytes(),0,-1);
				PayType payType = null;
				for(byte[] tmp:tmps){
					payType = SerializationUtil.deserialize(tmp);
					payTypeList.add(payType);
				}
			}else{
				payTypeList = payTypeMapper.getPaylist();
				for(PayType payType:payTypeList){
					jedis.rpush(key.getBytes(),SerializationUtil.serialize(payType));
				}
				jedis.expire(key.getBytes(), Constant.KEY_EXPIRETIME);
			}
		}finally{
			redisClient.returnResource(jedis);
		}
		return payTypeList;
	}
	
	@Override
	public List<CountryResp> getAllcountrys() {
		log.info("获取所有国家服务开始...");
		List<Country> countryList = Lists.newArrayList();
		String key = redisClient.generateKey(AreaMapper.class,"queryAllCountrys");
		Jedis jedis = redisClient.getResource();
		try{
			if (jedis.llen(key) != 0) {
				List<byte[]> tmps = jedis.lrange(key.getBytes(),0,-1);
				Country country = null;
				for(byte[] tmp:tmps){
					country = SerializationUtil.deserialize(tmp);
					countryList.add(country);
				}
			}else{
				countryList = areaMapper.queryAllCountrys();
				for(Country country:countryList){
					jedis.rpush(key.getBytes(),SerializationUtil.serialize(country));
				}
				jedis.expire(key.getBytes(), Constant.KEY_EXPIRETIME);
			}
		}finally{
			redisClient.returnResource(jedis);
		}
		
		return CollectionUtils.transfer(countryList, CountryResp.class, new MyTransfer<Country, CountryResp>() {

			@Override
			public void transfer(Country u, CountryResp v) {
				v.setCountryId(u.getId());
			}
		});
	}

	@Override
	public List<PcrResp> getPcrs() {
		log.info("获取国内所有省市区服务开始...");
		List<PcrInfo> t_pcrs = areaMapper.queryAllPcr();
		List<PcrResp> results = Lists.newArrayList();
		//省份哨兵
		String guard_provinceId = null;
		//城市哨兵
		String guard_cityId = null;
		for(PcrInfo tmp:t_pcrs){
			if(tmp.getProvinceId().equals(guard_provinceId)){
				List<CityResp> citys = results.get(results.size()-1).getCitys();
				if(tmp.getCityId().equals(guard_cityId)){
					//新增区
					CityResp city = citys.get(citys.size()-1);
					city.getRegions().add(new RegionResp(tmp.getRegion(), tmp.getRegionId()));
				}else{
					//新增市
					CityResp city = new CityResp(tmp.getCity(), tmp.getCityId());
					city.getRegions().add(new RegionResp(tmp.getRegion(), tmp.getRegionId()));
					citys.add(city);
					guard_cityId = tmp.getCityId(); 
				}
			}else{
				//新增省
				PcrResp province = new PcrResp(tmp.getProvince(), tmp.getProvinceId());
				CityResp city = new CityResp(tmp.getCity(), tmp.getCityId());
				RegionResp region = new RegionResp(tmp.getRegion(), tmp.getRegionId());
				city.getRegions().add(region);
				province.getCitys().add(city);
				results.add(province);
				guard_provinceId = tmp.getProvinceId();
				guard_cityId = tmp.getCityId(); 
			}
		}
		return results;
	}
	
	@Override
	public BigDecimal getFreight(String countryId,Integer limit){
		ChicunMoney freight = new ChicunMoney();
		if(Constant.CHINA.equals(countryId)){
			
		}else if(limit>2000){
			
		}else{
			freight = freight.add("150.00");
		}
		return freight.getMoney();
	}

}

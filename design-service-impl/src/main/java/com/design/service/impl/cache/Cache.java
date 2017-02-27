package com.design.service.impl.cache;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.design.common.utils.RedisClient;
import com.design.common.utils.SerializationUtil;
import com.design.dao.entity.City;
import com.design.dao.entity.Country;
import com.design.dao.entity.Province;
import com.design.dao.entity.Region;
import com.design.dao.persist.AreaMapper;
import com.google.common.collect.Lists;

@Service
public class Cache {
	@Resource
	private AreaMapper areaMapper;
	@Resource
	private RedisClient redisClient;
	
	public List<Country> queryAllCountrys() {
		String key = redisClient.generateKey(AreaMapper.class,"queryAllCountrys");
		Jedis jedis = redisClient.getResource();
		if (jedis.llen(key) != 0) {
			List<byte[]> tmps = jedis.lrange(key.getBytes(),0,-1);
			List<Country> countrys = Lists.newArrayList();
			Country country = null;
			for(byte[] tmp:tmps){
				System.out.println(tmp);
				country = SerializationUtil.deserialize(tmp);
				countrys.add(country);
			}
			return countrys;
		}else{
			List<Country> t_countrys = areaMapper.queryAllCountrys();
			for(Country tmp:t_countrys){
				jedis.lpush(key.getBytes(),SerializationUtil.serialize(tmp));
			}
			return t_countrys;
		}
	}
	public List<Province> queryAllProvince() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<City> queryAllCitysByProvinceId(Integer provinceId) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Region> queryAllRegionsByCityId(Integer regionId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

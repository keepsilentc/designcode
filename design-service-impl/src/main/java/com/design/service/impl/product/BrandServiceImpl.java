package com.design.service.impl.product;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.utils.DozerUtils;
import com.design.dao.entity.Brand;
import com.design.dao.persist.BrandMapper;
import com.design.service.api.IBrandService;
import com.design.service.api.dto.resp.BrandResp;
@Service
public class BrandServiceImpl implements IBrandService {
	private Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);
	@Resource
	private BrandMapper brandMapper;
	@Override
	public List<BrandResp> getBrands() {
		log.info("获取所有品牌服务开始...");
		List<Brand> t_brands = brandMapper.getBrands();
		return DozerUtils.transferList(t_brands, BrandResp.class);
	}

}

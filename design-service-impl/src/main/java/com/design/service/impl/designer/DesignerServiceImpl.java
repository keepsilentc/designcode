package com.design.service.impl.designer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.UserDesigner;
import com.design.dao.entity.dto.DesignerInfo;
import com.design.dao.entity.dto.MarkDesignerInfo;
import com.design.dao.entity.dto.ProductInfo;
import com.design.dao.persist.DesignerMapper;
import com.design.dao.persist.UserDesignerMapper;
import com.design.service.api.IDesignerService;
import com.design.service.api.dto.req.DesignersReq;
import com.design.service.api.dto.resp.DesignerDetailInfoResp;
import com.design.service.api.dto.resp.DesignerInfoResp;
import com.design.service.api.dto.resp.DesignerProductResp;
import com.design.service.api.dto.resp.DesignersResp;
import com.design.service.impl.product.ProductServiceImpl;
import com.design.service.impl.status.StatusServiceImpl;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class DesignerServiceImpl implements IDesignerService {
	private static Logger log = LoggerFactory.getLogger(DesignerServiceImpl.class);
	@Resource
	private DesignerMapper designerMapper;
	@Resource
	private UserDesignerMapper userdesignerMapper;
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Resource
	private ProductServiceImpl productServiceImpl;
	@Resource
	private StatusServiceImpl statusServiceImpl;
	
	@Override
	public List<DesignersResp> getDesigners(String token) {
		String userNo = null;
		if(StringUtils.isNotEmpty(token)){
			userNo = defaultUserServiceImpl.getUserNoByToken(token);
		}
		List<DesignerInfo> designers = designerMapper.getDesignersByUserNo(userNo);
		List<DesignersResp> result = Lists.newArrayList();
		String shortName;
		DesignersResp designerResp;
		Iterator<DesignerInfo> it = designers.iterator();
		DesignerInfo designerInfo = null;
		while(it.hasNext()){
			designerInfo = it.next();

			shortName = designerInfo.getDesignerName().substring(0,1);
			DesignerInfoResp designerInfoResp = DozerUtils.transfer(designerInfo, DesignerInfoResp.class);
			designerInfoResp.setDesignerId(String.valueOf(designerInfo.getId()));
			Map<String,Object> param_in = Maps.newHashMap();
			param_in.put("designerId", designerInfo.getId());
			param_in.put("pageIndex", 0);
			param_in.put("pageSize", 4);
			List<ProductInfo> designerProductList = productServiceImpl.getProductListByParam(param_in);
			
			if(CollectionUtils.isEmpty(designerProductList)){
				it.remove();
				continue;
			}
			
			designerInfoResp.setDesignerProducts(DozerUtils.transferList(designerProductList, DesignerProductResp.class));
			
			if(result.size()>0){
				designerResp = result.get(result.size()-1);
				if(designerResp.getShortName().equals(shortName)){
					designerResp.getDesigners().add(designerInfoResp);
				}else{
					designerResp = new DesignersResp();
					designerResp.setShortName(shortName);
					designerResp.setDesigners(new ArrayList<DesignerInfoResp>());
					designerResp.getDesigners().add(designerInfoResp);
					result.add(designerResp);
				}
			}else{
				designerResp = new DesignersResp();
				designerResp.setShortName(shortName);
				designerResp.setDesigners(new ArrayList<DesignerInfoResp>());
				designerResp.getDesigners().add(designerInfoResp);
				result.add(designerResp);
			}
		}
		
		return result;
	}
	
	@Override
	public List<DesignersResp> searchDesigners(DesignersReq req) {
		log.info("按条件获取设计师服务开始,{}",req);
		Map<String,String> param = Maps.newHashMap();
		if (StringUtils.isNotEmpty(req.getCountryId())) {
			param.put("countryId", req.getCountryId());
		}
		if (StringUtils.isNotEmpty(req.getDesignerName())) {
			param.put("designerName", req.getDesignerName());
		}
		if (StringUtils.isNotEmpty(req.getDesignerId())) {
			param.put("designerId", req.getDesignerId());
		}
		List<DesignerInfo> designers = designerMapper.getDesignersByCondition(param);
		List<DesignersResp> result = Lists.newArrayList();
		String shortName;
		DesignersResp designerResp;
		for(DesignerInfo designerInfo:designers){
			shortName = designerInfo.getDesignerName().substring(0,1);
			DesignerInfoResp designerInfoResp = DozerUtils.transfer(designerInfo, DesignerInfoResp.class);
			designerInfoResp.setDesignerId(String.valueOf(designerInfo.getId()));
			
			if(result.size()>0){
				designerResp = result.get(result.size()-1);
				if(designerResp.getShortName().equals(shortName)){
					designerResp.getDesigners().add(designerInfoResp);
				}else{
					designerResp = new DesignersResp();
					designerResp.setShortName(shortName);
					designerResp.setDesigners(new ArrayList<DesignerInfoResp>());
					designerResp.getDesigners().add(designerInfoResp);
					result.add(designerResp);
				}
			}else{
				designerResp = new DesignersResp();
				designerResp.setShortName(shortName);
				designerResp.setDesigners(new ArrayList<DesignerInfoResp>());
				designerResp.getDesigners().add(designerInfoResp);
				result.add(designerResp);
			}
		}
		return result;
	}
	
	@Override
	public DesignerDetailInfoResp getDesignInfo(String token,Long id) {
		log.info("获取具体设计师信息服务开始,{}",id);
		String userNo = null;
		if(StringUtils.isNotEmpty(token)){
			userNo = defaultUserServiceImpl.getUserNoByToken(token);
		}
		DesignerInfo t_designerInfo = getDesignerDetailInfoById(userNo,id);
		Integer praiseNum = userdesignerMapper.getPraiseNumByDesignerId(id);
		DesignerDetailInfoResp resp = DozerUtils.transfer(t_designerInfo, DesignerDetailInfoResp.class);
		resp.setFollowNum(String.valueOf(praiseNum));
		resp.setProductList(productServiceImpl.getAllProducts(id));
		resp.setStatusList(statusServiceImpl.getAllStatus(id));
		return resp; 
	}
	
	/**
	 * 获取设计师详细信息,如果不存在或未启用则抛异常
	 * @param id
	 * @return
	 */
	private DesignerInfo getDesignerDetailInfoById(String userNo,Long id) {
		DesignerInfo t_designerInfo = designerMapper.getDesignerDetailInfoById(userNo,id);
		if(t_designerInfo==null){
			throw new DesignException(DesignEx.DESIGNERNOTFIND);
		}else if(Constant.UNENABLE.equals(t_designerInfo.getIsEnable())){
			throw new DesignException(DesignEx.DESIGNEUNABLE);
		}
		return t_designerInfo;
	}


	@Override
	public void follow(String token, Long designerId,Integer follow) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo", userNo);
		params.put("designerId", designerId);
		UserDesigner t_userDesigner = userdesignerMapper.getUserDesignerByCondition(params);
		
		if(follow==1){
			if(t_userDesigner==null){
				UserDesigner userDesigner = new UserDesigner();
				userDesigner.setDesignerId(designerId);
				userDesigner.setUserNo(userNo);
				userDesigner.setIsInterest(follow);
				userDesigner.setCreateTime(DateUtil.getCurrentDate());
				userdesignerMapper.insert(userDesigner);
			}
		}else if(follow==0){
			if(t_userDesigner!=null){
				userdesignerMapper.del(t_userDesigner);
			}
		}
	}

	public Integer getMarkDesignerNum(String userNo) {
		return userdesignerMapper.getMarkDesignerNum(userNo);
	}

	public List<MarkDesignerInfo> getMarkDesigners(String userNo) {
		return designerMapper.getMarkDesigners(userNo);
	}

}

package com.design.service.impl.status;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.design.common.assist.Constant;
import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.CollectionUtils;
import com.design.common.utils.DateUtil;
import com.design.common.utils.DozerUtils;
import com.design.common.utils.MyTransfer;
import com.design.common.utils.StringUtils;
import com.design.dao.entity.User;
import com.design.dao.entity.UserStatus;
import com.design.dao.entity.dto.StatusDetailInfo;
import com.design.dao.entity.dto.StatusInfo;
import com.design.dao.persist.StatusMapper;
import com.design.dao.persist.UserStatusMapper;
import com.design.service.api.IStatusService;
import com.design.service.api.dto.req.StatusReq;
import com.design.service.api.dto.resp.DesignerStatusResp;
import com.design.service.api.dto.resp.StatusDetailResp;
import com.design.service.api.dto.resp.StatusListResp;
import com.design.service.impl.user.DefaultUserServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class StatusServiceImpl implements IStatusService{
	
	private static Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);
	@Resource
	private StatusMapper statusMapper;
	@Resource
	private UserStatusMapper userStatusMapper;
	@Resource
	private DefaultUserServiceImpl defaultUserServiceImpl;
	@Value(value = "${upload.path}")
	private String uploadPath;
	@Value(value = "${aliyun.imgpath}")
	private String aliyunimapath;
	
	@Override
	public List<StatusListResp> getStatusList(String token,StatusReq req) {
		String userNo = null;
		if(StringUtils.isNotEmpty(token)){
			userNo = defaultUserServiceImpl.getUserNoByToken(token);
		}
		
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo",userNo);
		if(req.getPageSize()!=null){
			params.put("pageSize",req.getPageSize());
		}else{
			params.put("pageSize", Constant.PAGESIZE);
		}
		if(req.getPageIndex()!=null){
			params.put("pageIndex", (req.getPageIndex()-1)*(Integer)params.get("pageSize"));
		}else{
			params.put("pageIndex", 0);
		}
		List<StatusInfo> t_statusList =  statusMapper.getStatusList(params);
		return CollectionUtils.transfer(t_statusList, StatusListResp.class, new MyTransfer<StatusInfo, StatusListResp>() {
			@Override
			public void transfer(StatusInfo u, StatusListResp v) {
				v.setStatusId(String.valueOf(u.getId()));
			}
		});
	}
	
	@Override
	public void markStatus(String token, Long statusId, Integer mark) {
		String userNo = defaultUserServiceImpl.getUserNoByToken(token);
		getStatusById(statusId);
		Map<String,Object> params = Maps.newHashMap();
		params.put("userNo", userNo);
		params.put("statusId", statusId);
		UserStatus t_userStatus = userStatusMapper.getUserStatusByCondition(params);
		if(mark==1){
			if(t_userStatus==null){
				UserStatus userStatus = new UserStatus();
				userStatus.setIsPraise(mark);
				userStatus.setUserNo(userNo);
				userStatus.setStatusId(statusId);
				userStatus.setCreateTime(DateUtil.getCurrentDate());
				userStatusMapper.insert(userStatus);
			}
		}else if(mark==0){
			if(t_userStatus!=null){
				userStatusMapper.del(t_userStatus);
			}
		}
	}
	
	
	private void getStatusById(Long statusId) {
		
	}
	
	
	
	
	@Override
	public StatusDetailResp getStatusDetail(String token, Long id) {
		String userNo = null;
		if(StringUtils.isNotEmpty(token)){
			userNo = defaultUserServiceImpl.getUserNoByToken(token);
		}
		log.info("获取动态详情,{}",userNo);
		StatusDetailInfo statusDetail = getEnableStatusDetailById(id,userNo);
		String path = uploadPath+"statusDetailText"+File.separator;
		String content = null;
		try {
			content = FileUtils.readFileToString(new File(path+statusDetail.getStatusDetailUrl()),"UTF-8");
			content = content.replaceAll("/design/cms/file/get\\.do\\?id=",this.aliyunimapath);
		} catch (IOException e) {
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		StatusDetailResp statusDetailResp = DozerUtils.transfer(statusDetail, StatusDetailResp.class);
		statusDetailResp.setStatusId(String.valueOf(statusDetail.getId()));
		statusDetailResp.setHtmlStr(content);
		Long markCount = statusDetail.getMarkCount();
		if(markCount >0){
			UserStatus t_userStatus = userStatusMapper.getLatestUserByStatusId(statusDetail.getId());
			User t_user = defaultUserServiceImpl.getUserByUserNo(t_userStatus.getUserNo());
			statusDetailResp.setGoodsCount(String.valueOf(markCount));
			statusDetailResp.setLatestGUAvatar(String.valueOf(t_user.getPhoto()));
			statusDetailResp.setLatestGUName(t_user.getNickName());
		}
		
		return statusDetailResp;
	}
	
	@Override
	public String getShareContent(Long statusId) {
		StatusDetailInfo statusDetail = getEnableStatusDetailById(statusId,null);
		String path = uploadPath+File.separator+"statusDetailText"+File.separator;
		String content = null;
		try {
			content = FileUtils.readFileToString(new File(path+statusDetail.getStatusDetailUrl()),"UTF-8");
			content = content.replaceAll("/design/cms/file/get\\.do\\?id=",aliyunimapath);
			return content;
		} catch (IOException e) {
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
	}
	
	public StatusDetailInfo getEnableStatusDetailById(Long id,String userNo) {
		StatusDetailInfo statusDetail = statusMapper.getStatusDetailInfoById(id,userNo);
		if(statusDetail==null){
			throw new DesignException(DesignEx.STATUSNOTFIND);
		}else if(Constant.UNENABLE.equals(statusDetail.getIsEnable())){
			throw new DesignException(DesignEx.STATUSEUABLE);
		}
		return statusDetail;
	}
	public List<DesignerStatusResp> getAllStatus(Long designerId) {
		List<DesignerStatusResp> result = Lists.newArrayList();
		DesignerStatusResp tmp = null;
		List<Map<String,Object>> designerStatus = statusMapper.getAllStatus(designerId);
		for(Map<String,Object> tmpMap:designerStatus){
			tmp = new DesignerStatusResp();
			tmp.setStatusId(String.valueOf(tmpMap.get("ID")));
			tmp.setPicture(String.valueOf(tmpMap.get("Picture")));
			tmp.setStatusName(String.valueOf(tmpMap.get("StatusName")));
			result.add(tmp);
		}
		return result;
	}
	
	public Integer getMarkStatusNum(String userNo) {
		return userStatusMapper.getMarkStatusNum(userNo);
	}
	public List<StatusInfo> getMarkStatus(String userNo) {
		return statusMapper.getMarkStatus(userNo);
	}

}

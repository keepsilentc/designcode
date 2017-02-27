package com.design.service.impl.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.design.common.utils.DozerUtils;
import com.design.dao.entity.Category;
import com.design.dao.persist.CategoryMapper;
import com.design.service.api.ICategoryService;
import com.design.service.api.dto.resp.CategoryResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service
public class CategoryServiceImpl implements ICategoryService {
	@Resource
	private CategoryMapper cateGoryMapper;
	@Override
	public List<CategoryResp> getCategorys() {
		List<Category> t_cateGorys =  cateGoryMapper.getAllCateGorys();
		//存放parentId对应的category 在list中的位置
		Map<Long,Integer> flagMap = Maps.newHashMap();
		List<CategoryResp> resp = Lists.newArrayList();
		for(Category tmp:t_cateGorys){
			//一级
			if(tmp.getParentId()==null){
				CategoryResp sup_cateGory = DozerUtils.transfer(tmp, CategoryResp.class);
				sup_cateGory.setCategoryId(String.valueOf(tmp.getId()));
				resp.add(sup_cateGory);
				flagMap.put(tmp.getId(), resp.size()-1);
			}else{
				Integer index = flagMap.get(tmp.getParentId());
				if(index!=null){
					CategoryResp sup_cateGory =  resp.get(index);
					CategoryResp sub_cateGory = DozerUtils.transfer(tmp, CategoryResp.class);
					sub_cateGory.setCategoryId(String.valueOf(tmp.getId()));
					if(sup_cateGory.getSubCategorys()==null){
						sup_cateGory.setSubCategorys(new ArrayList<CategoryResp>());
					}
					sup_cateGory.getSubCategorys().add(sub_cateGory);
				}
			}
		}
		return resp;
	}

}

package com.design.dao.persist;

import java.util.List;
import java.util.Map;

import com.design.dao.entity.Theme;
import com.design.dao.entity.dto.PreSellThemeInfo;


public interface ThemeMapper {

	List<PreSellThemeInfo> getPreSellThemes(Map<String, Object> param);

	Theme getThemeByProductNo(String productNo);
	
}

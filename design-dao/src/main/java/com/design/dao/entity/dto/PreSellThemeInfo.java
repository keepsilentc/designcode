package com.design.dao.entity.dto;

import java.util.Date;

import com.design.dao.entity.Theme;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSellThemeInfo extends Theme{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8138478164599447820L;
	
	private String designerName;
	private Long designerAvatar;
	private Date preSellEndTime;
}

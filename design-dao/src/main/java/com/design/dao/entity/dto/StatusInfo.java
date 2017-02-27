package com.design.dao.entity.dto;

import lombok.Getter;
import lombok.Setter;

import com.design.dao.entity.Status;
@Getter
@Setter
public class StatusInfo extends Status {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8909032100239292039L;
	
	private Long designerId;
	
	private String themeName;
	
	private String countryId;
	
	private Long markCount;
	
	private Integer isMarked;
	
	private String designerName;
	
	private Long designerAvatar;
	
	private Integer canBuy;
	
}

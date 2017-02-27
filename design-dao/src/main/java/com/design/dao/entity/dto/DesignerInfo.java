package com.design.dao.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.design.dao.entity.Designer;
@Getter
@Setter
@ToString
public class DesignerInfo extends Designer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -551648532441906597L;
	private String brandName;
	private Integer isFollow;
	
}

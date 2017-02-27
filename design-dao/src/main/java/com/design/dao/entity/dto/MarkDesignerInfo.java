package com.design.dao.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkDesignerInfo extends DesignerInfo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3805290537917698520L;
	
	private Long picture;
	private String productNo;
}

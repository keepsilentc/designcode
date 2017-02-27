package com.design.dao.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Level implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9914158941029349L;
	private String id;
	private String levelName;
	private String levelIcon;
}

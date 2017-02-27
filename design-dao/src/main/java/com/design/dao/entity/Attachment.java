package com.design.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Attachment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 380524480202905087L;
	private Long id;
	private String classify;
	private String fileName;
	private String filePath;
	private String serverFileName;
	private Date createTime;
	
}

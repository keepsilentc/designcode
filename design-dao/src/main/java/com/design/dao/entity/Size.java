package com.design.dao.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Size implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2272376792874673497L;
	private Long id;
	private Long countryId;
	private Long sizeTypeId;
	private String name;
	private int orderBy;
	
}

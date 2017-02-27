package com.design.service.api.dto.resp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class DesignersResp {
	
	private String shortName;
	
	List<DesignerInfoResp> designers ;

}

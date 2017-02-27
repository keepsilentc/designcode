package com.design.common.enums;

public enum DynamicType {
	PRESELL("STATUSTYPE001","预售/销售"),
	BRAND_INTRODUCE("STATUSTYPE002","品牌介绍"),
	COLLOCATION("STATUSTYPE003","搭配");
	
	private String typeCode;
	
	@SuppressWarnings("unused")
	private String typeName;
	
	private DynamicType(String typeCode, String typeName) {
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
	
	public static DynamicType getDynamicType(String typeCode){
		for(DynamicType tmp:DynamicType.values()){
			if(tmp.typeCode.equals(typeCode)){
				return tmp;
			}
		}
		return null;
	}
}

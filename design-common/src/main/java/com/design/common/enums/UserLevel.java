package com.design.common.enums;

public enum UserLevel {
	LEVEL_1("LEVEL0000","一级","Level1.png"),
	LEVEL_2("LEVEL0001","二级","Level2.png"),
	LEVEL_3("LEVEL0002","三级","Level3.png");
	
	private String levelId;
	private String levelName;
	private String levelIcon;
	private UserLevel(String levelId, String levelName, String levelIcon) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.levelIcon = levelIcon;
	}
	
	public static UserLevel getLevel(String levelId){
		for(UserLevel tmp:values()){
			if(tmp.levelId.equalsIgnoreCase(levelId)){
				return tmp;
			}
		}
		return null;
	}
	
	public String getLevelId() {
		return levelId;
	}
	public String getLevelName() {
		return levelName;
	}
	public String getLevelIcon() {
		return levelIcon;
	}
}

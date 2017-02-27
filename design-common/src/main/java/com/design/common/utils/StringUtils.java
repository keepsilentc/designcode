package com.design.common.utils;


public class StringUtils {

	public static boolean isNotEmpty(String str) {
		return str!=null&&!"".equals(str);
	}
	public static boolean isEmpty(String str){
		return !isNotEmpty(str);
	}
	public static String trim(String str){
		return isEmpty(str)?str:str.trim();
	}
	
	 /**
     * 检查指定的字符串列表是否不为空。
     */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}
}

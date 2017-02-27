package com.design.common.assist;

public class Constant {
	
	/**
	 * 启用
	 */
	public static Integer ENABLE =1;
	/**
	 * 不启用
	 */
	public static Integer UNENABLE =0;
	/**
	 * 用户默认等级
	 */
	public static String LEVEL_ID_INIT = "LEVEL0000";
	
	public static String PLATFORM_TYPE_INIT="DESIGN";
	/**
	 * redis 用户邮箱
	 */
	public static String EMAIL="email";
	/**
	 * redis 第三方平台id
	 */
	public static String PLATEFORMID="plateformid";
	
	public static String REGISTERCODE = "registerCode";
	
	public static String RESETPWDCODE = "resetPwdCode";
	
	public static int REGISTEREXPIRE = 5*60;
	/**
	 * redis缓存时间
	 */
	public static final int KEY_EXPIRETIME = 60*60;
	/**
	 * token,7天有效时间
	 */
	public static final int TOKEN_EXPIRETIME = 60*60*24*7;
	/**
	 * 男
	 */
	public static final int MALE = 1;
	public static final int FEMALE = 2;
	public static final Object DEFAULT = "default";
	public static final int PAGESIZE = 10;
	public static final String SUCCESSCODE = "200";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	/**
	 * 订单自动关闭时间,1小时
	 */
	public static final int AUTOCLOSETIME = 1;
	/**
	 * 出库
	 */
	public static final Integer PULL = 1;
	/**
	 * 入库
	 */
	public static final Integer PUSH = 0;
	/**
	 * 失败状态
	 */
	public static final Integer FAIL_STATE = 0;
	/**
	 * 成功
	 */
	public static final Integer SUCCESS_STATE = 1;
	/**
	 * 定时任务关闭订单
	 */
	public static final String AUTOCLOSE = "超过指定时间未成功付款";
	/**
	 * 用户关闭订单
	 */
	public static final String USERCLOSE = "用户关闭订单";
	public static final String CHINA = "CN";
	public static final String CNY = "CNY";
	public static final int TRYTIMES = 3;
	public static final int ZERO = 0;
}

package com.design.common.enums;

public enum DesignEx {
	//系统相关60开头
	INTERNAL_ERROR("6000","内部错误","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	CONCURRENCY_ERROR("6001","并发异常","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	UNKOWN_TYPE("6002","未知的类型","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	SMS_SEND_FAIL("6003","短信未发送成功，请再次尝试！"),
	//用户相关61开头
	TOKEN_ERROR("6100","token失效","Rennervate！重新登录，恢复活力！"),
	USER_STATUS_ERROR("6101","用户状态异常"),
	MOBILE_ALREADY_REGISTERED("6102","手机号已被注册"),
	EMAILALREADY_REGISTERED("6102","邮箱已被注册"),
	UNREGISTERED("6103","登录失败，账号未注册"),
	USERPASSWORDERROR("6104","登陆失败,密码错误"),
	EMAILEMPTY("6105","登录失败，Email未填写"),
	REGISTERCODE_ERROR("6106","注册失败,验证码错误"),
	USER_NOT_FIND("6107","用户未注册"),
	RESETPWDCODE_ERROR("6108","重置密码失败,验证码错误"),
	//设计师相关70开头
	DESIGNERNOTFIND("7000","设计师不存在","Deletrius！设计师消失了，请之后尝试！"),
	DESIGNEUNABLE("7001","设计师未启用","Deletrius！设计师消失了，请之后尝试！"),
	//文章相关71开头
	STATUSNOTFIND("7100","文章不存在","Deletrius！文章不见踪影了，请之后尝试！"),
	STATUSEUABLE("7101","文章未启用","Deletrius！文章不见踪影了，请之后尝试！"),
	//商品相关63开头
	PRODUCTNOTFIND("6300","商品不存在","Evanesco！产品消失了，请之后尝试！"),
	PRODUCTUNABLE("6301","商品未启用","Evanesco！产品消失了，请之后尝试！"),
	PRESELLNOTSTART("6302","预售商品未开始","Evanesco！产品预售未开始，请之后尝试！"),
	PRESELLEXPIRE("6303","预售商品已过期","Evanesco！产品已过预售期，请之后尝试！"),
	//商品库相关64开头
	WAREHOUSENOTHISPRODUCT("6400","库存中未发现此商品","Evanesco！仅有的库存没有了，请之后尝试！"),
	WAREHOUSE_PRODUCT_UNABLE("6401","库存中此商品未启用","Evanesco！仅有的库存没有了，请之后尝试！"),
	SHORTINVENTORY("6402","库存不足","Accio！最后一件被人召唤走啦！默念咒语，召唤库存"),
	SHORTSALE("6403","销量不足","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	//购物车相关65开头
	PRODUCTNOTSAME("6500","商品号不一致","Reducio！产品有问题，迅速缩小了，请稍后查看！"), 
	NUM_BEYOND_LIMIT("6501","购物车数量不能超过50","Reducio！购物车记录太多啦！"),
	//订单相关68开通
	ORDER_NOCARTIDS("6800","订单购物车id不能为空","Reducio！产品有问题，迅速缩小了，请稍后查看！"),
	CART_IN_ORDER_NOT_IN_CART("6801","订单中的购物车id在购物车列表中不存在","Reducio！产品有问题，迅速缩小了，请稍后查看！"),
	UNKNOWN_ORDER_TYPE("6802","未知订单类型","Petrificus Totalus！订单被石化了，出现错误！"),
	ORDER_NOT_EXISTS("6803","订单不存在","Petrificus Totalus！订单被石化了，出现错误！"),
	ORDER_STEATE_REFUSED("6804","订单状态不支持此操作","Petrificus Totalus！订单被石化了，出现错误！"),
	ORDER_MONEY_NOTSAME("6806","订单总金额不相等","Petrificus Totalus！订单被石化了，出现错误！"),
	ORDER_CANOTCLOSE("6809","订单不能关闭","Immobulus！订单被定身了，无法关闭！"),
	ORDER_CLOSED("6810","订单已关闭"),
	RETADDRESS_NOT_EXIST("6811","收获地址不存在","Immobulus！订单被定身了，地址出错！"),
	//愿望清单69开头
	WISH_NOT_EXIST("6900","愿望清单记录不存在","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	//支付相关70开头
	ALIPAY_SIGN_FAIL("7000","支付宝签名失败","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	ALIPAY_ERROR("7001","调用支付宝异常","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	PAYTYPE_NOT_EXIST("7002","不存在的支付类型","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	PayPay_NOT_COMPLETED("7003","paypal支付未完成","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	//退款相关75开头
	REFUND_NOT_APPROVE("7500","退款状态异常,需要为审批通过","Stupefy！退款被咒晕了，需要审核，无法操作！"),
	ERROR_REFUND_NUM("7501","退款个数错误","Stupefy！退款个数不对，无法操作！"),
	NOT_REFUNDED("7502","存在未完成的退款","Stupefy！退款被咒晕了，不能申请退款！"),
	REFUND_NOT_EXISTS("7503","退款不存在","Stupefy！退款被咒晕了，退款不存在！"),
	REFUND_DONE("7504","退款已完成,不能重复退款","Stupefy！退款已完成，不能再退啦，老板要跑路了！"),
	//退换货相关76开头
	ERROR_RETURN_ORDER_STATE("7600","订单未发货，不可申请退换货"),
	ERROR_RETURN_NUM("7601","退换货个数错误","Stupefy！退换货个数不对，无法操作！"),
	RETURNING("7602","存在未退换货记录,不能退换货","Stupefy！退换货被咒晕了，需要审核，无法操作！"),
	RETURN_NOT_EXISTS("7603","退货不存在","Stupefy！退货被咒晕了，退货不存在！"),
	//优惠券相关80开头
	COUPON_NOTEXIST("8000","优惠券不存在","Point Me！指路错误，优惠券不能用！"),
	COUPON_NOTENABLE("8001","优惠券未启用","Point Me！指路错误，优惠券不能用！"),
	COUPON_NOTSTART("8002","优惠券时间未开始","Point Me！指路正确，但优惠券还未开放使用！"),
	COUPON_STOPPED("8003","优惠券已过期","Point Me！指路正确，但优惠券已经过期！"),
	COUPON_USER_NOTEXIST("8010","用户不存在优惠券","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	COUPON_USER_INVALID("8011","用户优惠券已失效","Point Me！指路正确，但优惠券已经失效！"),
	COUPON_ALL_RECEIVED("8012","优惠券已被领完"),
	COUPON_NOTUSED_RECEIVE("8013","用户存在此类型未使用的优惠券"),
	COUPON_REPEAT_RECEIVE("8014","此优惠券不能重复领取"),
	//文件相关90开头
	ATTACHMENET_NOTFIND("9000","未发现附件","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息"),
	FILE_NOTEXISTS("9001","文件不存在","Expercto Patronum ！召唤攻城狮，可以驱逐BUG信息")
	;
	private String errCode;
	private String errMsg;
	
	private DesignEx(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	private DesignEx(String errCode, String errMsg,String errMsgServer) {
		this.errCode = errCode;
		this.errMsg = errMsgServer;
	}
	public String getErrCode() {
		return errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
}

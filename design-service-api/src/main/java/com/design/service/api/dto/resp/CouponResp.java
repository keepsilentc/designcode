package com.design.service.api.dto.resp;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.alibaba.fastjson.annotation.JSONField;

@Getter
@Setter
@ToString
public class CouponResp {
	/**
	 * 优惠券名称
	 */
	private String couponName;
	/**
	 * 优惠券号
	 */
	private String couponNo;
	
	@JSONField(format="yyyy.MM.dd")
	private Date startTime;
	
	@JSONField(format="yyyy.MM.dd")
	private Date endTime;
	/**
	 * 是否可用
	 * 1,有效
	 * 0,不失效
	 */
	private String valid;
	/**
	 * 剩余数量
	 */
	private String remainQuantity;
	/**
	 * 总量
	 */
	private String sumNum;
	/**
	 * 优惠描述
	 */
	private String salesPromote;
	/**
	 * 备注
	 */
	private String remark;
}

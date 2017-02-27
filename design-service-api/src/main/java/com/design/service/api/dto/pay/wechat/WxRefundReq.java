package com.design.service.api.dto.pay.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XStreamAlias("xml")
public class WxRefundReq extends WxcommonReq<WxRefundResp>{
	//微信订单号
	private String transaction_id;
	//商户退款单号
	private String out_refund_no;
	//总金额	
	private int total_fee;
	//退款金额
	private int refund_fee;
	//操作员帐号, 默认为商户号
	private String op_user_id;
	
}

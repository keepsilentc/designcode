package com.design.dao.entity.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NumAndMoney {
	private Long num;
	private BigDecimal money;
}

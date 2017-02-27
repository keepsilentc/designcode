package com.design.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.design.common.utils.ChicunMoney;
import com.design.dao.entity.Currency;
import com.design.dao.persist.CurrencyMapper;
@Service
public class CurrencyServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);
	
	@Resource
	private CurrencyMapper currencyMapper;
	
	public BigDecimal transfer(String fromCurrencyId, BigDecimal fromMoney,String toCurrencyId) {
		if(fromCurrencyId.equals(toCurrencyId)){
			return fromMoney;
		}
		List<Currency> t_currencyList = currencyMapper.getAllCurrency();
		
		Currency fromCurrency = null;
		Currency toCurrency = null;
		for(Currency currency:t_currencyList){
			if(currency.getId().equals(fromCurrencyId)){
				fromCurrency = currency;
			}else if(currency.getId().equals(toCurrencyId)){
				toCurrency = currency;
			}
		}
		if(fromCurrency==null||toCurrency==null){
			log.info("错误的币种");
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
		
		ChicunMoney chicun_CNYMoney = new ChicunMoney(fromMoney).multiply(fromCurrency.getExchangeRate());
		
		return chicun_CNYMoney.divide(toCurrency.getExchangeRate()).getMoney();
	}

	public Currency getCurrency(String currencyId) {
		List<Currency> t_currencyList = currencyMapper.getAllCurrency();
		
		for(Currency currency:t_currencyList){
			if(currency.getId().equals(currencyId)){
				return currency;
			}
		}
		log.info("错误的币种");
		throw new DesignException(DesignEx.INTERNAL_ERROR);
	}
	
}

package com.design.service.api;

import java.math.BigDecimal;
import java.util.List;

import com.design.service.api.dto.resp.CarouselResp;
import com.design.service.api.dto.resp.CountryResp;
import com.design.service.api.dto.resp.PayTypeResp;
import com.design.service.api.dto.resp.PcrResp;

public interface ICommonService {

	List<PayTypeResp> getPayList();

	List<PcrResp> getPcrs();

	List<CountryResp> getAllcountrys();

	List<CarouselResp> getCarouselList();

	BigDecimal getFreight(String countryId,Integer limit);

}

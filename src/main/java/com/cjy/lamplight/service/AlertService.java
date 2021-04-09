package com.cjy.lamplight.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dto.Expert;

@Service
public class AlertService {
	
	@Autowired
	private ExpertService expertService;

	public void pushNewOrderToExpert(Map<String, Object> param) {
		
		//해당 지역 지도사 필터링
		String region = (String) param.get("region");
		List<Expert> expertsForPushAlert = expertService.getForPrintExpertsByRegion(region);

	}

}

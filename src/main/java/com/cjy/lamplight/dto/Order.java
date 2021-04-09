package com.cjy.lamplight.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Order {

	private int id;
	private String regDate;
	private String updateDate;
	private String religion;
	private String startDate;
	private String endDate;
	private String deceasedName;
	private String bereavedName;
	private String funeralHome;
	private String region;
	private String body;
	private int expertId;
	private int clientId;
	private int stepLevel;

	private String extra__clientName;
	private String extra__expertName;
	private String extra__clientCellphoneNo;
	private String extra__expertCellphoneNo;

	private Map<String, Object> extra;

	public Map<String, Object> getExtraNotNull() {
		// 만약에 추가 정보가 없으면 새로운 Map 객체 생성 후 리턴
		if (extra == null) {
			extra = new HashMap<String, Object>();
		}

		return extra;
	}

	public Order(int id, String regDate, String updateDate, String religion,
			String startDate, String endDate, String deceasedName, String bereavedName, String funeralHome, String region, String body, int expertId, int clientId, int stepLevel) {

		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.religion = religion;
		this.startDate = startDate;
		this.endDate = endDate;
		this.deceasedName = deceasedName;
		this.bereavedName = bereavedName;
		this.funeralHome = funeralHome;
		this.region = region;
		this.body = body;
		this.expertId = expertId;
		this.clientId = clientId;
		this.stepLevel = stepLevel;

	}

}

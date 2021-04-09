package com.cjy.lamplight.dto;


import java.util.ArrayList;
import java.util.List;

import com.cjy.lamplight.service.ExpertService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
public class Expert {
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	@JsonIgnore
	//@JsonIgnore: json으로 보이지 않게 할 수 있다.
	private String loginPw;
	//@JsonIgnore
	private int acknowledgment_step;
	@JsonIgnore
	private String authKey;
	private String name;
	private String cellphoneNo;
	private String email;
	private String region;
	private String license;
	private String career;

	
	private String extra__thumbImg;
	private float extra__ratingPoint;
	private List<Review> extra__reviews = new ArrayList<Review>();
	
	public String getAcknowledgmentStepName() {
		return ExpertService.getAcknowledgmentStepName(this);
	}
	public String getAcknowledgmentStepNameColor() {
		return ExpertService.getAcknowledgmentStepNameColor(this);
	}

}
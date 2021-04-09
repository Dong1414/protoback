package com.cjy.lamplight.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.RatingDao;
import com.cjy.lamplight.dto.Client;
import com.cjy.lamplight.dto.Rating;
import com.cjy.lamplight.dto.ResultData;

@Service
public class RatingService {
	@Autowired
	private RatingDao ratingDao;
	
	public ResultData addRating(Map<String, Object> param) {
		ratingDao.addRating(param);

		return new ResultData("S-1", "평점이 등록되었습니다.");
	}

	public Rating getRating(int id) {
		return ratingDao.getRating(id);
	}

	public ResultData getActorCanDeleteRd(Rating Rating, Client actor) {
		if (Rating.getClientId() == actor.getId()) {
			return new ResultData("S-1", "가능합니다.");
		}

		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData deleteRating(Map<String, Object> param) {
		ratingDao.deleteRating(param);

		return new ResultData("S-1", "삭제하였습니다.");
	}

	public ResultData getActorCanModifyRd(Rating Rating, Client actor) {
		return getActorCanDeleteRd(Rating, actor);
	}

	public ResultData getRatingRelClient(String relTypeCode, Integer relId, Integer clientId) {
		Rating rating = ratingDao.getRatingRelClient(relTypeCode,relId,clientId);
		
		return new ResultData("S-1", "성공", "rating", rating);
	}

	public ResultData modifyRating(Map<String, Object> param) {
		ratingDao.modifyRating(param);
		return new ResultData("S-1", "성공");
	}
}

package com.cjy.lamplight.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.ReviewDao;
import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.Review;
import com.cjy.lamplight.util.Util;
import com.cjy.lamplight.dto.ResultData;

@Service
public class ReviewService {
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private MemberService memberService;
	@Autowired
	private RatingService ratingService;
	
	public ResultData addReview(Map<String, Object> param) {
		reviewDao.addReview(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "리뷰가 작성되었습니다.", "id", id);
	}

	public List<Review> getForPrintReviews(String relTypeCode) {
		return reviewDao.getForPrintReviews(relTypeCode);
	}

	public Review getReview(int id) {
		return reviewDao.getReview(id);
	}

	public ResultData getActorCanDeleteRd(Review review, Member actor) {
		if (review.getClientId() == actor.getId()) {
			return new ResultData("S-1", "가능합니다.");
		}

		if (memberService.isAdmin(actor)) {
			return new ResultData("S-2", "가능합니다.");
		}

		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData deleteReview(Map<String, Object> param) {
		int id = Util.getAsInt(param.get("id"), 0);
		reviewDao.deleteReview(id);
		
		ratingService.deleteRating(param);
		
		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	public ResultData getActorCanModifyRd(Review review, Member actor) {
		return getActorCanDeleteRd(review, actor);
	}

	public boolean isClientCanReview(int clientId, int relId) {
		Review review = reviewDao.getReviewByClientIdAndRelId(clientId, relId);
		if(review != null) {
			return false;
		}
		return true;
	}

	public ResultData modifyReview(Map<String, Object> param) {
		reviewDao.modifyReview(param);
		return new ResultData("S-1", "리뷰를 수정하였습니다.");
	}

	public ResultData getForPrintReview(Integer id) {
		Review review = reviewDao.getForPrintReview(id);
		return new ResultData("S-1", "성공", "review", review);
	}
}

package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Review;

@Mapper
public interface ReviewDao {

	List<Review> getForPrintReviews(@Param("relTypeCode") String relTypeCode);
	Review getReview(@Param("id")int id);
	void deleteReview(@Param("id")int id);
	void addReview(Map<String, Object> param);
	Review getReviewByClientIdAndRelId(@Param("clientId") int clientId, @Param("relId") int relId);
	void modifyReview(Map<String, Object> param);
	Review getForPrintReview(@Param("id")Integer id);

}

package com.cjy.lamplight.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Rating;

@Mapper
public interface RatingDao {

	Rating getRating(@Param("id")int id);
	void deleteRating(Map<String, Object> param);
	void addRating(Map<String, Object> param);
	Rating getRatingRelClient(@Param("relTypeCode")String relTypeCode, @Param("relId")Integer relId,@Param("clientId")Integer clientId);
	void modifyRating(Map<String, Object> param);

}

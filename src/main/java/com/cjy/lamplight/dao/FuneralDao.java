package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Funeral;
import com.cjy.lamplight.dto.ResultData;

@Mapper
public interface FuneralDao {

	List<Funeral> getForPrintFuneralsByMemberId(Map<String, Object> param);

	List<Funeral> getForPrintFunerals();
	
	void asstApplyForFuneral(Map<String, Object> param);

	Integer getAssistantIdByFuneralId(@Param("funeralId") int funeralId);

	void addFuneral(Map<String, Object> param);

	void asstCancleApplyForFuneral(@Param("funeralId") Integer funeralId, @Param("assistantId") Integer assistantId);

}

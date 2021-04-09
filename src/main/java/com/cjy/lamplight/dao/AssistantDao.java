package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Assistant;

@Mapper
public interface AssistantDao {

	List<Assistant> getForPrintAssistants();

	void join(Map<String, Object> param);

	Assistant getAssistant(@Param("id") int id);
	
	Assistant getForPrintAssistant(@Param("id") int id);

	Assistant getAssistantByLoginId(@Param("loginId") String loginId);

	void modifyAssistant(Map<String, Object> param);

	Assistant getAssistantByAuthKey(@Param("authKey") String authKey);

	List<Assistant> getAssistants();


}

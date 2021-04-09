package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Member;

@Mapper
public interface MemberDao {

	List<Member> getForPrintMembers(Map<String, Object> param);

	void join(Map<String, Object> param);

	Member getMember(@Param("id") int id);
	
	Member getForPrintMember(@Param("id") int id);

	Member getMemberByLoginId(@Param("loginId") String loginId);

	void modifyMember(Map<String, Object> param);

	Member getMemberByAuthKey(@Param("authKey") String authKey);

	List<Member> getMembers();

	List<Member> getDirectors();



}

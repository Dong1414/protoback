package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Client;

@Mapper
public interface ClientDao {

	List<Client> getForPrintClients(Map<String, Object> param);

	void join(Map<String, Object> param);

	Client getClient(@Param("id") int id);
	
	Client getForPrintClient(@Param("id") int id);

	Client getClientByLoginId(@Param("loginId") String loginId);

	void modifyClient(Map<String, Object> param);

	Client getClientByAuthKey(@Param("authKey") String authKey);

	List<Client> getClients();

	Client getClientByNameAndEmail(Map<String, Object> param);

	Client getMemberByLoginIdAndEmail(Map<String, Object> param);

}

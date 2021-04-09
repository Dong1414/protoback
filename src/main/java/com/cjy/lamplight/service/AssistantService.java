package com.cjy.lamplight.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.AssistantDao;
import com.cjy.lamplight.dto.Assistant;
import com.cjy.lamplight.dto.GenFile;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.util.Util;

@Service
public class AssistantService {

	@Autowired
	AssistantDao assistantDao;
	@Autowired
	GenFileService genFileService;

	public ResultData join(Map<String, Object> param) {
		assistantDao.join(param);

		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", param.get("name") + "님, 환영합니다.", "id", id);
	}

	public Assistant getAssistant(int id) {
		return assistantDao.getAssistant(id);
	}

	public Assistant getAssistantByLoginId(String loginId) {
		return assistantDao.getAssistantByLoginId(loginId);
	}

	public ResultData modifyAssistant(Map<String, Object> param) {
		assistantDao.modifyAssistant(param);
		
		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

	public Assistant getAssistantByAuthKey(String authKey) {
		return assistantDao.getAssistantByAuthKey(authKey);
	}

	public List<Assistant> getForPrintAssistants(){
		return assistantDao.getForPrintAssistants();
	}

	public Assistant getForPrintAssistant(int id) {
		Assistant assistant = assistantDao.getForPrintAssistant(id);
		
		updateForPrint(assistant);

		return assistant;
	}

	public Assistant getForPrintAssistantByAuthKey(String authKey) {
		Assistant assistant = assistantDao.getAssistantByAuthKey(authKey);
		// 기본 멤버에서 추가정보를 업데이트해서 리턴
		updateForPrint(assistant);

		return assistant;
	}

	public Assistant getForPrintAssistantByLoginId(String loginId) {
		Assistant assistant = assistantDao.getAssistantByLoginId(loginId);
		// 기본 멤버에서 추가정보를 업데이트해서 리턴
		updateForPrint(assistant);

		return assistant;
	}

	// 기본멤버 정보에 추가 정보를 업데이트해서 리턴
	private void updateForPrint(Assistant assistant) {
		// 멤버의 섬네일 이미지 가져오기
		GenFile genFile = genFileService.getGenFile("assistant", assistant.getId(), "common", "attachment", 1);

		// 만약, 멤버의 섬네일 이미지가 있으면 extra__thumbImg 업데이트
		if (genFile != null) {
			String imgUrl = genFile.getForPrintUrl();
			assistant.setExtra__thumbImg(imgUrl);
		}

	}

	public List<Assistant> getAssistants() {
		List<Assistant> assistants = assistantDao.getAssistants();
		
		for(Assistant assistant : assistants) {
			updateForPrint(assistant);
		}
		
		return assistants;
	}

}

package com.cjy.lamplight.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.FuneralDao;
import com.cjy.lamplight.dto.Assistant;
import com.cjy.lamplight.dto.Funeral;
import com.cjy.lamplight.dto.Order;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.dto.Review;
import com.cjy.lamplight.util.Util;

@Service
public class FuneralService {

	@Autowired
	private FuneralDao funeralDao;
	@Autowired
	private AssistantService assistantService;
	
	public List<Funeral> getForPrintFunerals() {
		List<Funeral> funerals = funeralDao.getForPrintFunerals();
		
		for(Funeral funeral : funerals) {
			addAssistants(funeral);
		}
		
		return funerals;
	}

	public List<Funeral> getForPrintFuneralsByMemberId(Map<String, Object> param) {
		List<Funeral> funerals = funeralDao.getForPrintFuneralsByMemberId(param);
		
		for(Funeral funeral : funerals) {
			addAssistants(funeral);
		}
		
		return funerals;
	}

	private void addAssistants(Funeral funeral) {
		List<Assistant> assistants = assistantService.getForPrintAssistants();
		
		for(Assistant assistant : assistants) {
				if(assistant.getExtra__relFuneralId() == funeral.getId()) {
					funeral.getExtra__assistants().add(assistant);
				}	
		}
	}
	
	public boolean isDupApplyAsst(Map<String, Object> param) {
		int funeralId = Util.getAsInt(param.get("funeralId"), 0);
		int assistantId = Util.getAsInt(param.get("assistantId"), 0);
		
		Integer dupAssistantId = funeralDao.getAssistantIdByFuneralId(funeralId);
		
		if(dupAssistantId != null && dupAssistantId == assistantId) {
			return true;
		}
		
		return false;
	}

	public ResultData asstApplyForFuneral(Map<String, Object> param) {
		funeralDao.asstApplyForFuneral(param);
		
		String msg = param.get("funeralId") + "번 장례에 지원하셨습니다.";
		
		return new ResultData("S-1", msg);
	}

	public void addFuneral(Map<String, Object> param) {
		funeralDao.addFuneral(param);
	}

	public ResultData asstCancleApplyForFuneral(Integer funeralId, Integer assistantId) {

		funeralDao.asstCancleApplyForFuneral(funeralId, assistantId);

		String msg = funeralId + "번 장례 지원을 취소했습니다.";
		return new ResultData("S-1", msg);
	}

	


	
	/*
	 * public Funeral getFuneral(int id) { return funeralDao.getFuneral(id); }
	 * 
	 * public List<Funeral> getFunerals() { return funeralDao.getFunerals(); }
	 * 
	 * public ResultData addFuneral(Map<String, Object> param) {
	 * funeralDao.addFuneral(param);
	 * 
	 * int id = Util.getAsInt(param.get("id"), 0);
	 * 
	 * return new ResultData("S-1", "성공하였습니다.", "id", id); }
	 * 
	 * public ResultData deleteFuneral(int id) { funeralDao.deleteFuneral(id);
	 * 
	 * // 게시물에 달린 첨부파일도 같이 삭제 // 1. DB에서 삭제 // 2. 저장소에서 삭제
	 * genFileService.deleteGenFiles("funeral", id);
	 * 
	 * return new ResultData("S-1", "삭제하였습니다.", "id", id); }
	 * 
	 * public ResultData modifyFuneral(Map<String, Object> param) {
	 * funeralDao.modifyFuneral(param);
	 * 
	 * int id = Util.getAsInt(param.get("id"), 0);
	 * 
	 * return new ResultData("S-1", "요청서를 수정하였습니다.", "id", id); }
	 * 
	 * public Funeral getForPrintFuneral(Integer id) { return
	 * funeralDao.getForPrintFuneral(id); }
	 * 
	 * 
	 * 
	 * public ResultData getClientCanDeleteRd(Funeral funeral, Client client) {
	 * return getActorCanModifyRd(funeral, client); }
	 * 
	 * public ResultData getExpertanDeleteRd(Funeral funeral, Expert expert) {
	 * return getActorCanModifyRd(funeral, expert); }
	 * 
	 * public ResultData getActorCanModifyRd(Funeral funeral, Object actor) {
	 * 
	 * // 1. 의뢰인 본인인 경우 if (actor instanceof Client) { Client client = (Client)
	 * actor; if (funeral.getClientId() == client.getId()) { return new
	 * ResultData("S-1", "가능합니다."); } } // 2. 전문가 본인인 경우 if (actor instanceof
	 * Expert)
	 * 
	 * { Expert expert = (Expert) actor; if (funeral.getExpertId() ==
	 * expert.getId()) { return new ResultData("S-1", "가능합니다."); } }
	 * 
	 * return new ResultData("F-1","권한이 없습니다."); }
	 */

}

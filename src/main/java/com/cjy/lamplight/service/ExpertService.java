package com.cjy.lamplight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.ExpertDao;
import com.cjy.lamplight.dto.Client;
import com.cjy.lamplight.dto.Expert;
import com.cjy.lamplight.dto.GenFile;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.dto.Review;
import com.cjy.lamplight.util.Util;

@Service
public class ExpertService {

	@Autowired
	ExpertDao expertDao;
	@Autowired
	GenFileService genFileService;
	@Autowired
	private ReviewService reviewService;

	public ResultData join(Map<String, Object> param) {
		expertDao.join(param);

		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", param.get("name") + "님, 환영합니다.", "id", id);
	}

	public Expert getExpert(int id) {
		return expertDao.getExpert(id);
	}

	public Expert getExpertByLoginId(String loginId) {
		return expertDao.getExpertByLoginId(loginId);
	}

	public ResultData modifyExpert(Map<String, Object> param) {
		expertDao.modifyExpert(param);
		
		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

	public Expert getExpertByAuthKey(String authKey) {
		return expertDao.getExpertByAuthKey(authKey);
	}

	public static String getAcknowledgmentStepName(Expert expert) {
		switch (expert.getAcknowledgment_step()) {
		case 3:
			return "가입실패";
		case 2:
			return "가입승인";
		case 1:
			return "가입대기";
		default:
			return "유형 정보 없음";
		}
	}

	public static String getAcknowledgmentStepNameColor(Expert expert) {
		switch (expert.getAcknowledgment_step()) {
		case 3:
			return "red";
		case 2:
			return "gray";
		case 1:
			return "blue";
		default:
			return "";
		}
	}

	public Expert getForPrintExpert(int id) {
		Expert expert = expertDao.getForPrintExpert(id);
		
		updateForPrint(expert);

		return expert;
	}

	public Expert getForPrintExpertByAuthKey(String authKey) {
		Expert expert = expertDao.getExpertByAuthKey(authKey);

		updateForPrint(expert);

		return expert;
	}

	public Expert getForPrintExpertByLoginId(String loginId) {
		Expert expert = expertDao.getExpertByLoginId(loginId);

		updateForPrint(expert);

		return expert;
	}

	// 기본멤버 정보에 추가 정보를 업데이트해서 리턴
	private void updateForPrint(Expert expert) {
		// 멤버의 섬네일 이미지 가져오기
		GenFile genFile = genFileService.getGenFile("expert", expert.getId(), "common", "attachment", 1);

		// 만약, 멤버의 섬네일 이미지가 있으면 extra__thumbImg 업데이트
		if (genFile != null) {
			String imgUrl = genFile.getForPrintUrl();
			expert.setExtra__thumbImg(imgUrl);
		}

	}


	public List<Expert> getExperts() {
		List<Expert> experts = expertDao.getExperts();
		
		for(Expert expert : experts) {
			updateForPrint(expert);
			addReviewList(expert); //각 expert객체마다 review리스트를 담아서 넘겨줌
		}
		
		return experts;
	}

	private void addReviewList(Expert expert) {
		String relTypeCode = "expert";
		List<Review> reviews = reviewService.getForPrintReviews(relTypeCode);
		
		for(Review review : reviews) {
			if(review != null && review.getRelId() == expert.getId()) {
				expert.getExtra__reviews().add(review);
			}
		}

	}

	public List<Expert> getForPrintExpertsByRegion(String region) {
		return expertDao.getForPrintExpertsByRegion(region);
	}

	public void setWork2(Integer expertId) {
		expertDao.setWork2(expertId);
		
	}

	public void setWork1(Integer expertId) {
		expertDao.setWork1(expertId);
		
	}

	public ResultData findLoginIdByNameAndEmail(Map<String, Object> param) {
		// 해당 이름과 이메일주소를 가진 회원이 존재하는지 확인
		Expert expert = expertDao.getExpertByNameAndEmail(param);

		if (expert == null) {
			return new ResultData("F-2", "일치하는 회원이 존재하지 않습니다.");
		}
		
		String name = expert.getName();
		// 로그인아이디 알림창 보여주고 로그인화면으로 이동
		return new ResultData("S-1", name + "회원님의 아이디는 \"" + expert.getLoginId() + "\"입니다.", "loginId", expert.getLoginId());
	}

	public ResultData getClientByLoginIdAndEmail(Map<String, Object> param) {
		Expert expert = expertDao.getMemberByLoginIdAndEmail(param);

		if (expert == null) {
			return new ResultData("F-2", "일치하는 회원이 존재하지 않습니다.");
		}

		// 임시 비밀번호 생성 후 회원 PW수정
		ResultData createTempLoginPwAndUpdateInfoRs = createTempLoginPwAndUpdateInfo(expert);

		// 임시패스워드 발급 알림
		return new ResultData("S-1", createTempLoginPwAndUpdateInfoRs.getMsg());
	}

	// 임시패스워드 발급
	public ResultData createTempLoginPwAndUpdateInfo(Expert actor) {
		
		// 임시패스워드 생성
		String tempPassword = Util.getTempPassword(6);

		// 발급받은 임시패스워드로 회원 정보 업데이트
		setTempPassword(actor, tempPassword);

		String resultMsg = "회원님의 임시 비밀번호는 \"" + tempPassword + "\"입니다.";
		
		return new ResultData("S-1", resultMsg);
	}
	
	private void setTempPassword(Expert actor, String tempPassword) {
		Map<String, Object> modifyArg = new HashMap<>();
		modifyArg.put("id", actor.getId());
		modifyArg.put("loginPw", tempPassword);

		// 회원정보 수정
		modifyClient(modifyArg);
	}
	
	public ResultData modifyClient(Map<String, Object> param) {
		expertDao.modifyExpert(param);

		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

}

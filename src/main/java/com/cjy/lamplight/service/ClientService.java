package com.cjy.lamplight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.lamplight.dao.ClientDao;
import com.cjy.lamplight.dto.Client;
import com.cjy.lamplight.dto.GenFile;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.util.Util;

@Service
public class ClientService {

	@Autowired
	ClientDao clientDao;

	@Autowired
	GenFileService genFileService;

	public ResultData join(Map<String, Object> param) {
		clientDao.join(param);

		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", param.get("name") + "님, 환영합니다.", "id", id);
	}

	public Client getClient(int id) {
		return clientDao.getClient(id);
	}

	public Client getClientByLoginId(String loginId) {
		return clientDao.getClientByLoginId(loginId);
	}

	public ResultData modifyClient(Map<String, Object> param) {
		clientDao.modifyClient(param);

		int id = Util.getAsInt(param.get("id"), 0);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

	public Client getClientByAuthKey(String authKey) {
		return clientDao.getClientByAuthKey(authKey);
	}

	public Client getForPrintClient(int id) {
		Client client = clientDao.getForPrintClient(id);

		updateForPrint(client);

		return client;
	}

	public Client getForPrintClientByAuthKey(String authKey) {
		Client client = clientDao.getClientByAuthKey(authKey);
		// 기본 멤버에서 추가정보를 업데이트해서 리턴
		updateForPrint(client);

		return client;
	}

	public Client getForPrintClientByLoginId(String loginId) {
		Client client = clientDao.getClientByLoginId(loginId);
		// 기본 멤버에서 추가정보를 업데이트해서 리턴
		updateForPrint(client);

		return client;
	}

	// 기본멤버 정보에 추가 정보를 업데이트해서 리턴
	private void updateForPrint(Client client) {
		// 멤버의 섬네일 이미지 가져오기
		GenFile genFile = genFileService.getGenFile("client", client.getId(), "common", "attachment", 1);

		System.out.println("111111111111111111111111111");
		// 만약, 멤버의 섬네일 이미지가 있으면 extra__thumbImg 업데이트
		if (genFile != null) {

			System.out.println("이미지 가져옴22222222222222222222222");
			String imgUrl = genFile.getForPrintUrl();
			client.setExtra__thumbImg(imgUrl);
		}

	}

	public List<Client> getClients() {
		return clientDao.getClients();
	}

	public ResultData findLoginIdByNameAndEmail(Map<String, Object> param) {
		// 해당 이름과 이메일주소를 가진 회원이 존재하는지 확인
		Client client = clientDao.getClientByNameAndEmail(param);

		if (client == null) {
			return new ResultData("F-2", "일치하는 회원이 존재하지 않습니다.");
		}
		
		String name = client.getName();

		// 로그인아이디 알림창 보여주고 로그인화면으로 이동
		return new ResultData("S-1", name + "회원님의 아이디는 \"" + client.getLoginId() + "\"입니다.", "loginId", client.getLoginId());
	}

	public ResultData getClientByLoginIdAndEmail(Map<String, Object> param) {
		
		Client client = clientDao.getMemberByLoginIdAndEmail(param);

		if (client == null) {
			return new ResultData("F-2", "일치하는 회원이 존재하지 않습니다.");
		}

		// 임시 비밀번호 생성 후 회원 PW수정
		ResultData createTempLoginPwAndUpdateInfoRs = createTempLoginPwAndUpdateInfo(client);

		// 임시패스워드 발급 알림
		return new ResultData("S-1", createTempLoginPwAndUpdateInfoRs.getMsg());
	}

	// 임시패스워드 발급
	public ResultData createTempLoginPwAndUpdateInfo(Client actor) {
		
		// 임시패스워드 생성
		String tempPassword = Util.getTempPassword(6);

		// 발급받은 임시패스워드로 회원 정보 업데이트
		setTempPassword(actor, tempPassword);

		String resultMsg = "회원님의 임시 비밀번호는 \"" + tempPassword + "\"입니다.";
		
		return new ResultData("S-1", resultMsg);

	}

	private void setTempPassword(Client actor, String tempPassword) {
		Map<String, Object> modifyArg = new HashMap<>();
		modifyArg.put("id", actor.getId());
		modifyArg.put("loginPw", tempPassword);

		// 회원정보 수정
		modifyClient(modifyArg);
	}

}

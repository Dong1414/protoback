package com.cjy.lamplight.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.lamplight.dto.Expert;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.service.ExpertService;
import com.cjy.lamplight.util.Util;

@Controller
public class UsrExpertController extends BaseController {

	@Autowired
	private ExpertService expertService;
	
	@GetMapping("/usr/expert/list")
	@ResponseBody
	public ResultData showExpertList(HttpServletRequest req) {

		List<Expert> experts = expertService.getExperts();

		req.setAttribute("experts", experts);	
		
		return new ResultData("S-1", "성공", "experts", experts);
	}
	
	@GetMapping("/usr/expert/detail")
	@ResponseBody
	public ResultData showExpertDetail(HttpServletRequest req, int id) {

		Expert expert = expertService.getForPrintExpert(id);

		req.setAttribute("expert", expert);	
		
		//return "/usr/expert/detail";
		return new ResultData("S-1", "성공", "expert", expert);
	}

	@RequestMapping("/usr/expert/join")
	public String showJoin() {
		return "usr/expert/join";
	}
	
	@PostMapping("/usr/expert/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Expert existingExpert = expertService.getExpertByLoginId((String) param.get("loginId"));

		if (existingExpert != null) {
			return new ResultData("F-1", "이미 사용중인 로그인아이디 입니다.");
		}
		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}
		if (param.get("name") == null) {
			return new ResultData("F-1", "name을 입력해주세요.");
		}
		if (param.get("email") == null) {
			return new ResultData("F-1", "email을 입력해주세요.");
		}
		if (param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "cellphoneNo를 입력해주세요.");
		}
		if (param.get("region") == null) {
			return new ResultData("F-1", "region을 입력해주세요.");
		}
		if (param.get("license") == null) {
			return new ResultData("F-1", "license을 입력해주세요.");
		}						
		
		return expertService.join(param);
	}

	@GetMapping("/usr/expert/expertByAuthKey")
	@ResponseBody
	public ResultData showExpertByAuthKey(String authKey) {
		if (authKey == null) {
			return new ResultData("F-1", "authKey를 입력해주세요.");
		}

		Expert existingExpert = expertService.getForPrintExpertByAuthKey(authKey);

		if (existingExpert == null) {
			return new ResultData("F-2", "유효하지 않은 authKey입니다.");
		}
		return new ResultData("S-1", String.format("유효한 회원입니다."), "expert", existingExpert);
	}

	@PostMapping("/usr/expert/authKey")
	@ResponseBody
	public ResultData showAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Expert existingExpert = expertService.getForPrintExpertByLoginId(loginId);

		if (existingExpert == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}
		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}
		if (existingExpert.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingExpert.getName()), "authKey",
				existingExpert.getAuthKey(), "expert", existingExpert);
	}
	
	@RequestMapping("/usr/expert/login")
	public String showLogin() {
		return "usr/expert/login";
	}

	@RequestMapping("/usr/expert/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {

		if (loginId == null) {
			// return new ResultData("F-1", "loginId를 입력해주세요.");
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Expert existingExpert = expertService.getExpertByLoginId(loginId);

		if (existingExpert == null) {
			return Util.msgAndBack("존재하지 않는 로그인아이디 입니다.");
		}

		if (loginPw == null) {
			return Util.msgAndBack("loginPw를 입력해주세요.");
		}

		if (existingExpert.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		session.setAttribute("loginedExpertId", existingExpert.getId());

		String msg = String.format("%s님 환영합니다.", existingExpert.getName());

		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@RequestMapping("/usr/expert/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedExpertId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../home/main");
	}

	@PostMapping("/usr/expert/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {

		if (param.isEmpty()) {
			return new ResultData("F-2", "수정할 회원정보를 입력해주세요.");
		}

		return expertService.modifyExpert(param);
	}
	
	@GetMapping("/usr/expert/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {
		if (loginId == null) {
			return new ResultData("F-5", "loginId를 입력해주세요.");
		}

		if (Util.allNumberString(loginId)) {
			return new ResultData("F-3", "로그인아이디는 숫자만으로 구성될 수 없습니다.");
		}

		if (Util.startsWithNumberString(loginId)) {
			return new ResultData("F-4", "로그인아이디는 숫자로 시작할 수 없습니다.");
		}

		if (loginId.length() < 5) {
			return new ResultData("F-5", "로그인아이디는 5자 이상으로 입력해주세요.");
		}

		if (loginId.length() > 20) {
			return new ResultData("F-6", "로그인아이디는 20자 이하로 입력해주세요.");
		}

		if (Util.isStandardLoginIdString(loginId) == false) {
			return new ResultData("F-1", "로그인아이디는 영문소문자와 숫자의 조합으로 구성되어야 합니다.");
		}

		Expert existingExpert = expertService.getExpertByLoginId(loginId);

		if (existingExpert != null) {
			return new ResultData("F-2", String.format("%s(은)는 이미 사용중인 로그인아이디 입니다.", loginId));
		}

		return new ResultData("S-1", String.format("%s(은)는 사용가능한 로그인아이디 입니다.", loginId), "loginId", loginId);
	}
	
	@PostMapping("/usr/expert/doFindLoginId")
	@ResponseBody
	public ResultData doFindLoginId(@RequestParam Map<String, Object> param) {

		String name = (String) param.get("name");
		if (Util.isEmpty(name)) {
			return new ResultData("F-1", "name을 입력해주세요.");
		}

		String email = (String) param.get("email");
		if (Util.isEmpty(email)) {
			return new ResultData("F-1", "email을 입력해주세요.");
		}

		return expertService.findLoginIdByNameAndEmail(param);
	}

	@PostMapping("/usr/expert/doFindLoginPw")
	@ResponseBody
	public ResultData doFindLoginPw(@RequestParam Map<String, Object> param) {
		
		String loginId = (String) param.get("loginId");
		if (Util.isEmpty(loginId)) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		String email = (String) param.get("email");
		if (Util.isEmpty(email)) {
			return new ResultData("F-1", "email을 입력해주세요.");
		}
		
		return expertService.getClientByLoginIdAndEmail(param);
	}

}

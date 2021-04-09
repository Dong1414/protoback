package com.cjy.lamplight.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.service.MemberService;
import com.cjy.lamplight.util.Util;

@Controller
public class UsrMemberController extends BaseController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/usr/member/list")
	//@ResponseBody
	public String showList(HttpServletRequest req) {

		List<Member> members = memberService.getMembers();

		req.setAttribute("members", members);	
		

		//return new ResultData("S-1", "성공", "members", members);
		
		return "/usr/member/list";
	}
	
	@GetMapping("/usr/director/list")
	@ResponseBody
	public ResultData showDirectorList(HttpServletRequest req) {

		List<Member> members = memberService.getDirectors();
		
		//System.out.println(members.get(9).getExtra__ratingPoint());

		req.setAttribute("members", members);	
		

		//return new ResultData("S-1", "성공", "members", members);
		
		//return "usr/director/list";
		return new ResultData("S-1", "성공", "members", members);
	}
	
	@GetMapping("/usr/director/profile")
	@ResponseBody
	public ResultData showDirectorDetail(HttpServletRequest req, int id) {

		Member member = memberService.getForPrintMember(id);

		req.setAttribute("member", member);	
		
		//return "/usr/member/detail";
		return new ResultData("S-1", "성공", "member", member);
	}

	
	@GetMapping("/usr/member/detail")
	@ResponseBody
	public ResultData showMemberDetail(HttpServletRequest req, int id) {

		Member member = memberService.getForPrintMember(id);

		req.setAttribute("member", member);	
		
		//return "/usr/member/detail";
		return new ResultData("S-1", "성공", "member", member);
	}

	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "usr/member/join";
	}
	
	@PostMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param) {

		if (param.get("loginId") == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getMemberByLoginId((String) param.get("loginId"));

		if (existingMember != null) {
			return Util.msgAndBack("이미 사용중인 로그인아이디 입니다.");
		}

		if (param.get("loginPw") == null) {
			return Util.msgAndBack("loginPw를 입력해주세요.");
		}

		if (param.get("name") == null) {
			return Util.msgAndBack("name을 입력해주세요.");
		}

		if (param.get("nickname") == null) {
			return Util.msgAndBack("nickname을 입력해주세요.");
		}

		if (param.get("email") == null) {
			return Util.msgAndBack("email을 입력해주세요.");
		}

		if (param.get("cellphoneNo") == null) {
			return Util.msgAndBack("cellphoneNo를 입력해주세요.");
		}
		
		

		memberService.join(param);

		String msg = String.format("%s님 환영합니다.", param.get("nickname"));

		String redirectUrl = Util.ifEmpty((String) param.get("redirectUrl"), "../member/login");

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@GetMapping("/usr/member/memberByAuthKey")
	@ResponseBody
	public ResultData showMemberByAuthKey(String authKey) {
		if (authKey == null) {
			return new ResultData("F-1", "authKey를 입력해주세요.");
		}

		Member existingMember = memberService.getForPrintMemberByAuthKey(authKey);

		if (existingMember == null) {
			return new ResultData("F-2", "유효하지 않은 authKey입니다.");
		}
		return new ResultData("S-1", String.format("유효한 회원입니다."), "member", existingMember);
	}

	@PostMapping("/usr/member/authKey")
	@ResponseBody
	public ResultData showAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getForPrintMemberByLoginId(loginId);

		if (existingMember == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()), "authKey",
				existingMember.getAuthKey(), "member", existingMember);
	}
	
	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {

		if (loginId == null) {
			// return new ResultData("F-1", "loginId를 입력해주세요.");
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember == null) {
			// return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
			return Util.msgAndBack("존재하지 않는 로그인아이디 입니다.");
		}

		if (loginPw == null) {
			// return new ResultData("F-1", "loginPw를 입력해주세요.");
			return Util.msgAndBack("loginPw를 입력해주세요.");
		}

		if (existingMember.getLoginPw().equals(loginPw) == false) {
			// return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");\
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		// 세션에 로그인 회원 id 등록
		session.setAttribute("loginedMemberId", existingMember.getId());

		String msg = String.format("%s님 환영합니다.", existingMember.getNickname());

		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");

		// return new ResultData("S-1", String.format("%s님 환영합니다.",
		// existingMember.getNickname()));
		return Util.msgAndReplace(msg, redirectUrl);
	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedMemberId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../home/main");
	}

	@PostMapping("/usr/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {

		if (param.isEmpty()) {
			return new ResultData("F-2", "수정할 회원정보를 입력해주세요.");
		}

		return memberService.modifyMember(param);
	}
	
	@GetMapping("/usr/member/getLoginIdDup")
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

		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember != null) {
			return new ResultData("F-2", String.format("%s(은)는 이미 사용중인 로그인아이디 입니다.", loginId));
		}

		return new ResultData("S-1", String.format("%s(은)는 사용가능한 로그인아이디 입니다.", loginId), "loginId", loginId);
	}

}

package com.cjy.lamplight.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cjy.lamplight.dto.Assistant;
import com.cjy.lamplight.dto.Client;
import com.cjy.lamplight.dto.Expert;
import com.cjy.lamplight.service.AssistantService;
import com.cjy.lamplight.service.ClientService;
import com.cjy.lamplight.service.ExpertService;
import com.cjy.lamplight.service.MemberService;
import com.cjy.lamplight.util.Util;

@Component("beforeActionInterceptor") // 컴포넌트 이름 설정
public class BeforeActionInterceptor implements HandlerInterceptor {

	@Autowired
	private ClientService clientService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private AssistantService assistantService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		/* requestUrl 관련 로직 시작 */
		// 기타 유용한 정보를 request에 담는다.
		Map<String, Object> param = Util.getParamMap(request);
		String paramJson = Util.toJsonStr(param);

		String requestUrl = request.getRequestURI();
		String queryString = request.getQueryString();

		if (queryString != null && queryString.length() > 0) {
			requestUrl += "?" + queryString;
		}

		String encodedRequestUrl = Util.getUrlEncoded(requestUrl);

		request.setAttribute("requestUrl", requestUrl);
		request.setAttribute("encodedRequestUrl", encodedRequestUrl);

		request.setAttribute("afterLoginUrl", requestUrl);
		request.setAttribute("encodedAfterLoginUrl", encodedRequestUrl);

		request.setAttribute("paramMap", param);
		request.setAttribute("paramJson", paramJson);
		/* requestUrl 관련 로직 끝 */

		int loginedClientId = 0;
		int loginedExpertId = 0;
		int loginedAssistantId = 0;
		
		Client loginedClient = null;
		Expert loginedExpert = null;
		Assistant loginedAssistant = null;
		
		
		String authKey = request.getParameter("authKey");

		// 파라미터로 authKey가 들어왔으면
		if (authKey != null && authKey.length() > 0) {
			String[] authKies = authKey.split("__");
			
			if(authKies[0].contains("1")) {
				loginedClient = clientService.getClientByAuthKey(authKey);
				if (loginedClient == null) {
					// 인증되지 않은 회원
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					// authKey가 일치한다면 인증된 회원
					request.setAttribute("authKeyStatus", "valid");
					// 인증된 회원의 id를 저장(=세션 만료와 상관없이 저장되는 정보)
					loginedClientId = loginedClient.getId();
				}
			}
			else if(authKies[0].contains("2")) {
				loginedExpert = expertService.getExpertByAuthKey(authKey);
				if (loginedExpert == null) {
					// 인증되지 않은 회원
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					// authKey가 일치한다면 인증된 회원
					request.setAttribute("authKeyStatus", "valid");
					// 인증된 회원의 id를 저장(=세션 만료와 상관없이 저장되는 정보)
					loginedExpertId = loginedExpert.getId();
				}
			}
			else if(authKies[0].contains("3")) {
				loginedAssistant = assistantService.getAssistantByAuthKey(authKey);
				if (loginedAssistant == null) {
					// 인증되지 않은 회원
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					// authKey가 일치한다면 인증된 회원
					request.setAttribute("authKeyStatus", "valid");
					// 인증된 회원의 id를 저장(=세션 만료와 상관없이 저장되는 정보)
					loginedAssistantId = loginedAssistant.getId();
				}
			}

			// authKey가 일치하는 회원이 없다면
			
		}
		// 파라미터로 authKey가 들어오지 않았다면
		else {
			// session 가져오기
			HttpSession session = request.getSession();
			request.setAttribute("authKeyStatus", "none");

			// session에 로그인 정보 저장(=세션이 만료되면 초기화되는 정보)
			if (session.getAttribute("loginedClientId") != null) {
				loginedClientId = (int) session.getAttribute("loginedClientId");
				loginedClient = clientService.getClient(loginedClientId);
			}
			else if (session.getAttribute("loginedExpertId") != null) {
				loginedExpertId = (int) session.getAttribute("loginedExpertId");
				loginedExpert = expertService.getExpert(loginedExpertId);
			}
			else if (session.getAttribute("loginedAssistantId") != null) {
				loginedAssistantId = (int) session.getAttribute("loginedAssistantId");
				loginedAssistant = assistantService.getAssistant(loginedAssistantId);
			}
		}

		// 로그인 여부에 관련된 정보를 request에 담는다.
		boolean isLogined = false;
		boolean isAdmin = false;

		if (loginedClient != null || loginedExpert != null || loginedAssistant != null) {
			isLogined = true;		
		}


		request.setAttribute("loginedClientId", loginedClientId);
		request.setAttribute("loginedExpertId", loginedExpertId);
		request.setAttribute("loginedAssistantId", loginedAssistantId);
		request.setAttribute("loginedClient", loginedClient);
		request.setAttribute("loginedExpert", loginedExpert);
		request.setAttribute("loginedAssistant", loginedAssistant);
		request.setAttribute("isLogined", isLogined);
		request.setAttribute("isAdmin", isAdmin);
		

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
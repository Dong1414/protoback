package com.cjy.lamplight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//스프링이 시작되면 우선으로 읽어봐야되는 설정(config)이란 것을 알려주는 것
public class WebMvcConfig implements WebMvcConfigurer {
	// CORS 허용
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;

	// needAdminInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;

	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	// needLogoutInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;

	// 이 함수는 인터셉터를 적용하는 역할
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// beforeActionInterceptor 인터셉터가 모든 액션 실행전에 실행되도록 처리
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**").excludePathPatterns("/gen/**");

		// 관리자 로그인 필요
		registry.addInterceptor(needAdminInterceptor).addPathPatterns("/adm/**")
				.excludePathPatterns("/adm/member/login").excludePathPatterns("/adm/member/doLogin")
				.excludePathPatterns("/adm/member/join").excludePathPatterns("/adm/member/doJoin").excludePathPatterns("/adm/member/getLoginIdDup");

		// 로그인 필요
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/**").excludePathPatterns("/")
				.excludePathPatterns("/swagger-ui/**")
				.excludePathPatterns("/swagger-resources/**")
				.excludePathPatterns("/v2/api-docs")
				.excludePathPatterns("/webjars/**")
				.excludePathPatterns("/adm/**").excludePathPatterns("/gen/**").excludePathPatterns("/resource/**")
				.excludePathPatterns("/usr/home/main")
				.excludePathPatterns("/usr/client/authKey").excludePathPatterns("/usr/client/list")
				.excludePathPatterns("/usr/client/login").excludePathPatterns("/usr/client/doLogin").excludePathPatterns("/usr/client/detail")
				.excludePathPatterns("/usr/client/join").excludePathPatterns("/usr/client/doJoin").excludePathPatterns("/usr/client/getLoginIdDup")
				.excludePathPatterns("/usr/client/doFindLoginId").excludePathPatterns("/usr/client/doFindLoginPw")
				.excludePathPatterns("/usr/expert/authKey").excludePathPatterns("/usr/expert/list")
				.excludePathPatterns("/usr/expert/login").excludePathPatterns("/usr/expert/doLogin").excludePathPatterns("/usr/expert/detail")
				.excludePathPatterns("/usr/expert/join").excludePathPatterns("/usr/expert/doJoin").excludePathPatterns("/usr/expert/getLoginIdDup")
				.excludePathPatterns("/usr/expert/findLoginId").excludePathPatterns("/usr/expert/doFindLoginId").excludePathPatterns("/usr/expert/findLoginPw")
				.excludePathPatterns("/usr/expert/doFindLoginPw")
				.excludePathPatterns("/usr/assistant/authKey").excludePathPatterns("/usr/assistant/list")
				.excludePathPatterns("/usr/assistant/login").excludePathPatterns("/usr/assistant/doLogin").excludePathPatterns("/usr/assistant/detail")
				.excludePathPatterns("/usr/assistant/join").excludePathPatterns("/usr/assistant/doJoin").excludePathPatterns("/usr/assistant/getLoginIdDup")
				.excludePathPatterns("/usr/assistant/findLoginId").excludePathPatterns("/usr/assistant/doFindLoginId").excludePathPatterns("/usr/assistant/findLoginPw")
				.excludePathPatterns("/usr/assistant/doFindLoginPw")
				.excludePathPatterns("/usr/order/list").excludePathPatterns("/usr/order/detail")
				.excludePathPatterns("/usr/funeral/list")
				.excludePathPatterns("/usr/review/list")
				.excludePathPatterns("/common/**").excludePathPatterns("/usr/file/test*")
				.excludePathPatterns("/usr/file/doTest*").excludePathPatterns("/test/**").excludePathPatterns("/error");

		// 로그인 상태에서 접속할 수 없는 URL 전부 기술
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/adm/member/login")
				.addPathPatterns("/adm/member/doLogin")
				.addPathPatterns("/usr/client/login").addPathPatterns("/usr/client/doLogin")
				.addPathPatterns("/usr/client/join").addPathPatterns("/usr/client/doJoin")
				.addPathPatterns("/usr/expert/login").addPathPatterns("/usr/expert/doLogin")
				.addPathPatterns("/usr/expert/join").addPathPatterns("/usr/expert/doJoin")
				.addPathPatterns("/usr/assistant/login").addPathPatterns("/usr/assistant/doLogin")
				.addPathPatterns("/usr/assistant/join").addPathPatterns("/usr/assistant/doJoin");
		}
		
	// 파일첨부를 위한 경로 가져오기 위한 로직
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**").addResourceLocations("file:///" + genFileDirPath + "/")
					.setCachePeriod(20);
			
	}
}
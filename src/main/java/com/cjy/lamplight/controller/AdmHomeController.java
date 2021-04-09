package com.cjy.lamplight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//어노테이션
//이 클래스가 컨트롤러 클래스라는것을 알려주는 것
//어떤 클래스를 만들면 스프링에게 이 클래스의 용도를 알려주어야 함
public class AdmHomeController {
	
	// http://localhost:8024/usr/home/main
	@RequestMapping("/adm/home/main")
	//@ResponseBody
	// 만약 "/usr/home/main" 요청이 들어오면 showMain()가 실행되도록 하는 것
	public String showMain() {
		return "adm/home/main";
	}
}

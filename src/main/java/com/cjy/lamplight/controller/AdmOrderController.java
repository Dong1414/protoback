package com.cjy.lamplight.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.cjy.lamplight.dto.Order;
import com.cjy.lamplight.dto.Board;
import com.cjy.lamplight.dto.GenFile;
import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.service.OrderService;
import com.cjy.lamplight.service.GenFileService;
import com.cjy.lamplight.util.Util;

@Controller
public class AdmOrderController extends BaseController{

	@Autowired
	private OrderService orderService;
	@Autowired
	private GenFileService genFileService;

	@RequestMapping("/adm/order/detail")
	@ResponseBody
	// 스프링부트: 알아서 json형태로 바꿔 출력값을 리턴해준다.
	public ResultData showDetail(Integer id) {
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Order order = orderService.getForPrintOrder(id);

		if (order == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호 입니다.");
		}
	
		return new ResultData("S-1", "성공", "order", order);
	}

	@RequestMapping("/adm/order/list")
	//@ResponseBody
	public String showList(HttpServletRequest req){
		List<Order> orders = orderService.getForPrintOrders();
		
		req.setAttribute("orders", orders);

		return "adm/order/list";
	}
	
	@RequestMapping("/adm/order/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/order/add";
	}

	@RequestMapping("/adm/order/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req, MultipartRequest multipartRequest) {
		//HttpSession session을 HttpServletRequest req로 교체, 인터셉터에서 session 정보를 Request에 담음으로 
		//session을 가져올 필요 없이 req로 값을 받으면 됨
		
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");

		if (param.get("title") == null) {
			return msgAndBack(req, "title을 입력해주세요.");
		}
		if (param.get("body") == null) {
			return msgAndBack(req, "body를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);
		
		ResultData addOrderRd = orderService.addOrder(param);
		
		// addOrderRd map의 body에서 key값이 id인 것을 가져와라
		int newOrderId = (int) addOrderRd.getBody().get("id");
		
		
		/* 이미 ajax상에서 처리하므로 더이상 필요 없음
		//MultipartRequest : 첨부파일 기능 관련 요청
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); //MultipartRequest로 들어온 map 정보를 가져오기
				
		
		//fileMap.keySet() : file__order__0__common__attachment__1
		for (String fileInputName : fileMap.keySet()) {
			//fileInputName : file__order__0__common__attachment__1
			MultipartFile multipartFile = fileMap.get(fileInputName);
			
			if(multipartFile.isEmpty() == false) {
				//저장할 파일관련 정보를 넘김
				genFileService.save(multipartFile, newOrderId);
			}
			
		}
		*/
		return msgAndReplace(req, newOrderId + "번 게시물이 생성되었습니다.", "../order/detail?id=" + newOrderId);
	}

	@RequestMapping("/adm/order/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		// int 기본타입 -> null이 들어갈 수 없음
		// Integer 객체타입 -> null이 들어갈 수 있음
		//int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Order order = orderService.getOrder(id);

		if (order == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}
		
		/*
		 * ResultData actorCanDeleteRd = orderService.getActorCanDeleteRd(order,
		 * loginedMember);
		 * 
		 * if (actorCanDeleteRd.isFail()) { return actorCanDeleteRd; }
		 */
		return orderService.deleteOrder(id);
	}
	
	@RequestMapping("/adm/order/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Order order = orderService.getForPrintOrder(id);
		
		if (order == null) {
			return msgAndBack(req, "존재하지 않는 게시물번호 입니다.");
		}

		List<GenFile> genfiles = genFileService.getGenFiles("order", order.getId(), "common", "attachment");

		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile genfile : genfiles) {
			filesMap.put(genfile.getFileNo() + "", genfile);
		}

		order.getExtraNotNull().put("file__common__attachment", filesMap);
		req.setAttribute("order", order);

		return "adm/order/modify";
	}
	

	@RequestMapping("/adm/order/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		// int 기본타입 -> null이 들어갈 수 없음
		// Integer 객체타입 -> null이 들어갈 수 있음
		
		//int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		
		int id = Util.getAsInt(param.get("id"), 0);

		if (id == 0) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		if (Util.isEmpty(param.get("title"))) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if (Util.isEmpty(param.get("body"))) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		Order order = orderService.getOrder(id);

		if (order == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
		}

		ResultData actorCanModifyRd = orderService.getActorCanModifyRd(order, loginedMember);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		
		return orderService.modifyOrder(param);
	}
	
}

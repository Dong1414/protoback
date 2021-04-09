package com.cjy.lamplight.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.lamplight.dto.Client;
import com.cjy.lamplight.dto.Expert;
import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.Order;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.service.ExpertService;
import com.cjy.lamplight.service.OrderService;
import com.cjy.lamplight.util.Util;

@Controller
public class UsrOrderController extends BaseController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ExpertService expertService;
	
	
	@GetMapping("/usr/order/reject")
	@ResponseBody
	public ResultData doReject(Integer orderId, Integer expertId) {
		
		if (orderId == null) {			
			return new ResultData("F-1", "id를 입력해주세요.");			
		}
		
		Order order = orderService.getForPrintOrder(orderId);
		
		if (order == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호 입니다.");
		}		
		
		orderService.orderReject(orderId, expertId);
		expertService.setWork1(expertId);
		
		return new ResultData("S-1", "요청을 취소하였습니다.", "id", orderId);
	}
	
	@GetMapping("/usr/order/accept")
	@ResponseBody
	public ResultData doAccept(Integer orderId, Integer expertId) {
		
		if (orderId == null) {			
			return new ResultData("F-1", "id를 입력해주세요.");			
		}
		
		Order order = orderService.getForPrintOrder(orderId);
		
		if (order == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호 입니다.");
		}
		if (order.getStepLevel() > 1) {
			return new ResultData("F-2", "이미 다른 지도사가 수락한 요청입니다.");
		}
		
		orderService.setSetp2(orderId, expertId);
		expertService.setWork2(expertId);
		
		return new ResultData("S-1", "요청을 수락하였습니다.", "id", orderId);
	}

	@GetMapping("/usr/order/detail")
	@ResponseBody
	// 스프링부트: 알아서 json형태로 바꿔 출력값을 리턴해준다.
	public ResultData showDetail(Integer id) {
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Order order = orderService.getForPrintOrder(id);

		if (order == null) {
			return new ResultData("F-2", "존재하지 않는 요청서번호 입니다.");
		}
	
		return new ResultData("S-1", "성공", "order", order);
	}

	@GetMapping("/usr/order/list")
	@ResponseBody
	public ResultData showList(HttpServletRequest req, int memberId, String memberType) {
		int clientId = 0;
		int expertId = 0;
		if(memberType.equals("client")) {
			clientId = memberId;
		}
		if(memberType.equals("expert")) {
			expertId = memberId;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("clientId", clientId);
		param.put("expertId", expertId);

		List<Order> orders = orderService.getForPrintOrdersByMemberId(param);

		req.setAttribute("orders", orders);
		
		return new ResultData("S-1", "성공", "orders", orders);
	}
	
	@GetMapping("/usr/order/expertOrderList")
	@ResponseBody
	public ResultData showExpertOrderList(HttpServletRequest req, int memberId, String memberType) {
		Expert expert = expertService.getExpert(memberId);
		
		if ( expert == null ) {
			return new ResultData("F-1", "존재하지 않는 회원 입니다.");
		}					

		List<Order> orders = orderService.getForPrintExpertOrders(memberId, expert.getRegion());
		if(orders == null) {			
			return null;
		}
		return new ResultData("S-1", "성공", "orders", orders);
	}
	
	@GetMapping("/usr/order/listForAsst")
	@ResponseBody
	public ResultData showListForAsst(HttpServletRequest req) {

		List<Order> orders = orderService.getForPrintOrders();

		req.setAttribute("orders", orders);
		
		return new ResultData("S-1", "성공", "orders", orders);
	}

	@RequestMapping("/usr/order/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "usr/order/add";
	}

	@PostMapping("/usr/order/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param) {

		ResultData addOrderRd = orderService.addOrder(param);

		int newOrderId = (int) addOrderRd.getBody().get("id");

		return new ResultData("S-1", "의뢰 요청이 완료되었습니다.", "id", newOrderId);
	}

	@GetMapping("/usr/order/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		// int 기본타입 -> null이 들어갈 수 없음
		// Integer 객체타입 -> null이 들어갈 수 있음
		//int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
		ResultData actorCanDeleteRd = new ResultData("F-1", "권한이 없습니다.");
		
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Order order = orderService.getOrder(id);
		
		if (order == null) {
			return new ResultData("F-1", "해당 요청서는 존재하지 않습니다.");
		}
		
		if(req.getAttribute("loginedClient") != null) {
			Client loginedClient = (Client) req.getAttribute("loginedClient");
			actorCanDeleteRd = orderService.getClientCanDeleteRd(order, loginedClient);
		}
		
		if(req.getAttribute("loginedExpert") != null) {
			Expert loginedExpert = (Expert) req.getAttribute("loginedExpert");
			actorCanDeleteRd = orderService.getExpertanDeleteRd(order, loginedExpert);
		}

		if (actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}

		return orderService.deleteOrder(id);
	}

	@PostMapping("/usr/order/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		
		ResultData actorCanModifyRd = new ResultData("F-1", "권한이 없습니다.");

		int id = Util.getAsInt(param.get("id"), 0);

		if (id == 0) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Order order = orderService.getOrder(id);

		if (order == null) {
			return new ResultData("F-1", "해당 요청서는 존재하지 않습니다.", "id", id);
		}
		
		if(req.getAttribute("loginedClient") != null) {
			Client loginedClient = (Client) req.getAttribute("loginedClient");
			actorCanModifyRd = orderService.getActorCanModifyRd(order, loginedClient);
		}
		
		if(req.getAttribute("loginedExpert") != null) {
			Expert loginedExpert = (Expert) req.getAttribute("loginedExpert");
			actorCanModifyRd = orderService.getActorCanModifyRd(order, loginedExpert);
		}

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		
		return orderService.modifyOrder(param);
	}
	
	@GetMapping("/usr/order/changeStepLevel")
	@ResponseBody
	public ResultData doChangeStepLevel(int id, int stepLevel) {
		
		System.out.println(stepLevel);
		int nextStepLevel = stepLevel + 1;

		return orderService.changeStepLevel(id, nextStepLevel); 
	}
	
	
}

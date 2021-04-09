package com.cjy.lamplight.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.lamplight.dto.Order;
import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.Review;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.service.OrderService;
import com.cjy.lamplight.service.ReviewService;

@Controller
public class AdmReviewController {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/adm/review/doAddReview")
	@ResponseBody
	public ResultData doAddReview(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		if (param.get("orderId") == null) {
			return new ResultData("F-1", "orderId를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		return reviewService.addReview(param);
	}

	@RequestMapping("/adm/review/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId) {

		if (relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		
		if (relId == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}

		if ( relTypeCode.equals("order") ) {
			Order order = orderService.getOrder(relId);

			if ( order == null ) {
				return new ResultData("F-1", "존재하지 않는 게시물 입니다.");
			}
		}

		List<Review> reviews = reviewService.getForPrintReviews(relTypeCode);

		return new ResultData("S-1", "성공", "reviews", reviews);
	}
	
	/*
	 * @RequestMapping("/adm/review/doDelete")
	 * 
	 * @ResponseBody public ResultData doDelete(Integer id, HttpServletRequest req)
	 * { //int loginedMemberId = (int) req.getAttribute("loginedMemberId"); Member
	 * loginedMember = (Member) req.getAttribute("loginedMember");
	 * 
	 * if (id == null) { return new ResultData("F-1", "id를 입력해주세요."); }
	 * 
	 * Review review = reviewService.getReview(id);
	 * 
	 * if (review == null) { return new ResultData("F-1", "해당 댓글은 존재하지 않습니다."); }
	 * 
	 * ResultData actorCanDeleteRd = reviewService.getActorCanDeleteRd(review,
	 * loginedMember);
	 * 
	 * if (actorCanDeleteRd.isFail()) { return actorCanDeleteRd; }
	 * 
	 * return reviewService.deleteReview(id); }
	 */
	
	
	
}

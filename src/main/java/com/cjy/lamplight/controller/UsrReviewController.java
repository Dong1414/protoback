package com.cjy.lamplight.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.lamplight.dto.Member;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.dto.Review;
import com.cjy.lamplight.service.MemberService;
import com.cjy.lamplight.service.ReviewService;
import com.cjy.lamplight.util.Util;

@Controller
public class UsrReviewController {
	@Autowired
	private ReviewService reviewService;
	
	
	@PostMapping("/usr/review/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param) {
		int clientId = Util.getAsInt(param.get("clientId"), 0);
		if(clientId == 0) {
			return new ResultData("F-1", "clientId를 확인해주세요.");
		}
		
		if (param.get("relId") == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		
		int relId = Util.getAsInt(param.get("relId"), 0);
		
		boolean isClientCanReview = reviewService.isClientCanReview(clientId, relId);
		
		if(isClientCanReview == false) {
			return new ResultData("F-2", "회원님은 이미 리뷰를 작성하셨습니다.");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		if (param.get("relTypeCode") == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}

	
		return reviewService.addReview(param);
	}

	@GetMapping("/usr/review/list")
	@ResponseBody
	public ResultData showList(String relTypeCode) {

		if (relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		

		List<Review> reviews = reviewService.getForPrintReviews(relTypeCode);

		return new ResultData("S-1", "성공", "reviews", reviews);
	}
	
	@GetMapping("/usr/review/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		return reviewService.getForPrintReview(id);
	}
	
	@GetMapping("/usr/review/doDelete")
	@ResponseBody
	public ResultData doDelete(String relTypeCode, Integer relId, Integer id, Integer clientId) {
		
		if (relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		if (relId == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if (clientId == null) {
			return new ResultData("F-1", "clientId를 입력해주세요.");
		}

		Review review = reviewService.getReview(id);

		if (review == null) {
			return new ResultData("F-1", "해당 리뷰는 존재하지 않습니다.");
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("relTypeCode", relTypeCode);
		param.put("relId", relId);
		param.put("id", id);
		param.put("clientId", clientId);

		return reviewService.deleteReview(param);
	}
	
	@PostMapping("/usr/review/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {
		int clientId = Util.getAsInt(param.get("clientId"), 0);
		if(clientId == 0) {
			return new ResultData("F-1", "clientId를 확인해주세요.");
		}
		
		if (param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
			

		return reviewService.modifyReview(param);
	}
}

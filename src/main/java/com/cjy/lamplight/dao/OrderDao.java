package com.cjy.lamplight.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cjy.lamplight.dto.Board;
import com.cjy.lamplight.dto.Order;

/* Mybatis 적용으로 삭제 */
//Mybatis틑 class가 아닌 interface를 인식함

//@Component
//public class OrderDao {

@Mapper
public interface OrderDao {
	/* Mybatis 적용으로 기존 내용 삭제 */
	//Mybatis에서 자동으로 만들어 줌
	Order getOrder(@Param("id") int id);
	List<Order> getOrders();
	List<Order> getForPrintOrders();
	void addOrder(Map<String, Object> param);
	void deleteOrder(@Param("id") int id);
	void modifyOrder(Map<String, Object> param);
	Order getForPrintOrder(@Param("id") int id);
	Board getBoard(@Param("id") int id);

	List<Order> getForPrintOrdersByMemberId(Map<String, Object> param);
	void changeStepLevel(@Param("id") int id, @Param("nextStepLevel") int nextStepLevel);
	
	List<Order> getForPrintExpertOrders(@Param("memberId") int memberId, @Param("region") String region);
	void setSetp2(@Param("orderId") Integer orderId, @Param("expertId") Integer expertId);
	void orderReject(@Param("orderId") Integer orderId, @Param("expertId") Integer expertId);
}

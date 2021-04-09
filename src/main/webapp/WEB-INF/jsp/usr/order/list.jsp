<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div>
			<c:forEach items="${orders}" var="order">
			<c:set var="detailUrl" value="detail?id=${order.id}" />
			<!-- 첨부파일1번 섬네일 이미지 URL -->
			<c:set var="thumbFileNo" value="${String.valueOf(1)}" />
			<c:set var="thumbFile" value="${order.extra.file__common__attachment[thumbFileNo]}" />
			<c:set var="thumbUrl" value="${thumbFile.getForPrintUrl()}" />
				<div class="flex items-center mt-10">
					<a href="${detailUrl}" class="font-bold">NO. ${order.id}</a>
					<a href="${detailUrl}" class="ml-2 font-light text-gray-600">${order.regDate}</a>
					<div class="flex-grow"></div>
					<a href="list"
						class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">리스트보기</a>
				</div>
				<div class="mt-2">
					<a href="${detailUrl}" class="text-2xl text-gray-700 font-bold hover:underline">${order.title}</a>
					<c:if test="${thumbUrl != null}">
						<a class="block" href="${detailUrl}" >
							<img class="max-w-sm" src="${thumbUrl}" alt="" />
						</a>
					</c:if>
					<a href="${detailUrl}" class="mt-2 text-gray-600 block">${order.body}</a>
				</div>
				<div class="flex items-center mt-4">
					<a href="detail?id=${order.id}" class="text-blue-500 hover:underline">자세히 보기</a>
					<a href="modify?id=${order.id}"
						class="ml-2 text-blue-500 hover:underline">수정</a>
					<a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;" href="doDelete?id=${order.id}"
						class="ml-2 text-blue-500 hover:underline">삭제</a>	
					<div class="flex-grow"></div>
					<div>
						<a class="flex items-center">
							<img
								src="https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=731&amp;q=80"
								alt="avatar" class="mx-4 w-10 h-10 object-cover rounded-full">
							<h1 class="text-gray-700 font-bold hover:underline">${order.extra__member}</h1>
						</a>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%> 
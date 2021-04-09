<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.cjy.lamplight.util.Util"%>

<%@ include file="../part/mainLayoutHead.jspf"%>


<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8 text-center">
		<div class="inline-flex justify-center items-center w-full px-4 py-2 rounded-full bg-gray-500 text-white">
			${member.authLevelName} 정보
		</div>
		<div class="flex flex-col items-center">
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-${member.authLevelNameColor}-600 text-${member.authLevelNameColor}-100 font-bold rounded hover:bg-${member.authLevelNameColor}-500">
					${member.authLevelName}
				</div>
				<div class="lg:flex-grow">
					${member.authLevel}
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					아이디
				</div>
				<div class="lg:flex-grow">
					${member.loginId}
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					이름
				</div>
				<div class="lg:flex-grow">
					${member.name}						
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					닉네임
				</div>
				<div class="lg:flex-grow">
					${member.nickname}					
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					지역
				</div>
				<div class="lg:flex-grow">
					${member.address}						
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					이메일
				</div>
				<div class="lg:flex-grow">
					${member.email}						
				</div>
			</div>
			<div class="flex flex-col items-center my-3 lg:flex-row">
				<div class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">
					연락처
				</div>
				<div class="lg:flex-grow">
					${member.cellphoneNo}						
				</div>
			</div>
		</div>
		<a onclick="history.back();" class="cursor-pointer btn-info bg-blue-500 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded inline-block">뒤로가기</a>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<c:set var="fileInputMaxCount" value="10"/>
<script>
OrderAdd__fileInputMaxCount = parseInt("${fileInputMaxCount}");
</script>

<script>

OrderAdd__submited = false;

function OrderAdd__checkAndSubmit(form) {
	
	if ( OrderAdd__submited ) {
		alert('처리중입니다.');
		return;
	}
	
	form.title.value = form.title.value.trim();
	if ( form.title.value.length == 0 ) {
		alert('제목을 입력해주세요.');
		form.title.focus();
		return false;
	}
/* 	form.body.value = form.body.value.trim();
	if ( form.body.value.length == 0 ) {
		alert('내용을 입력해주세요.');
		form.body.focus();
		return false;
	} */
	
	/* 첨부파일 용량 제한 쿼리 */
	var maxSizeMb = 50; 
	var maxSize = maxSizeMb * 1024 * 1024; //첨부파일 제한 용량을 50MB로 설정

	//반목문으로 변경
	for(let inputNo = 1; inputNo <= OrderAdd__fileInputMaxCount; inputNo++){
		//같은 의미? var input = form.file__order__0__common__attachment__ + inputNo;
		var input = form["file__order__0__common__attachment__" + inputNo];

		if (input.value) {
			if (input.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				input.focus();
				
				return;
			}
		}
	}


	/* 파일 업로드 */
	// 파일 업로드를 ajax로 하는 이유?
	// vue,안드로이드,리엑트 등은 파일전송을 ajax로 해야하기 때문
	const startUploadFiles = function(onSuccess) {

		var needToUpload = false;

		for(let inputNo = 1; inputNo <= OrderAdd__fileInputMaxCount; inputNo++){
			var input = form["file__order__0__common__attachment__" + inputNo];
			//form.file__order__0__common__attachment__1.value.length가 0보다 크면
			//즉, 첨부파일1이 있으면 needToUpload
			if(input.value.length > 0){
				needToUpload = true;
				break;
			}
		}
		
		//만약, needToUpload == false이면 리턴
		if (needToUpload == false) {
			onSuccess();  //일종의 콜백
			return;
		}

		//needToUpload가 있으면 즉, 파일 업로드가 필요하면 form을 만든다
		//ajax로 파일을 업로드하려면 이 방식으로 해야함
		//'form'안에 있는 정보가 자동으로 정리되어 fileUploadFormData 객체가 됨
		var fileUploadFormData = new FormData(form);
		$.ajax({
			url : '/common/genFile/doUpload',
			data : fileUploadFormData,
			processData : false,
			contentType : false,
			dataType : "json",
			type : 'POST',
			success : onSuccess  // => onSuccess == startSubmitForm, 통신이 성공하면 onSuccess 실행
		});
	}

	/* 파일업로드 완료 후 진행 */
	const startSubmitForm = function(data) {
		let genFileIdsStr = '';

		
		if (data && data.body && data.body.genFileIdsStr) {
			form.genFileIdsStr.value = data.body.genFileIdsStr;
		}

		
		form.file__order__0__common__attachment__1.value = '';
		form.file__order__0__common__attachment__2.value = '';
		
		form.submit();
	};
	
	OrderAdd__submited = true;
	//순서가 중요함
	//1.파일업로드(startUploadFiles)
	//2.파일업로드 후 실행되야 할 것(startSubmitForm)
	startUploadFiles(startSubmitForm);
}
</script>


<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div>
			요청페이지
		</div>
		<form onsubmit="OrderAdd__checkAndSubmit(this); return false;" action="doAdd" method="POST" enctype="multipart/form-data">
			<!-- 파일업로드를 하게되면 먼저 ajax로 전송을 하고 응답으로 value="1,2" 이런식으로 파일 번호가 들어옴-->
			<input type="hidden" name="genFileIdsStr" value="" />
			<input type="hidden" name="directorId" value="${param.directorId}" />
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>제목</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="title" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="제목을 입력해주세요." />
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>옵션1</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="option1" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="옵션을 입력해주세요." />
					<select name="option1qty" class="form-row-input w-full rounded-sm">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>옵션2</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="option2" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="옵션을 입력해주세요." />
					<select name="option2qty" class="form-row-input w-full rounded-sm">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>옵션3</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="option3" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="옵션을 입력해주세요." />
					<select name="option3qty" class="form-row-input w-full rounded-sm">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>옵션4</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="option4" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="옵션을 입력해주세요." />
					<select name="option4qty" class="form-row-input w-full rounded-sm">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>옵션5</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="option5" autofocus="autofocus"
						class="form-row-input w-full rounded-sm" placeholder="옵션을 입력해주세요." />
					<select name="option5qty" class="form-row-input w-full rounded-sm">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
			
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>추가 요청 사항</span>
				</div>
				<div class="lg:flex-grow">
					<textarea name="body" class="form-row-input w-full rounded-sm" placeholder="내용을 입력해주세요."></textarea>
				</div>
			</div>
			
			<c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>첨부파일 ${inputNo}</span>
				</div>
				<div class="lg:flex-grow">
					<input type="file" name="file__order__0__common__attachment__${inputNo}"
						class="form-row-input w-full rounded-sm" />
				</div>
			</div>
			</c:forEach>
			
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					
				</div>
				<div class="lg:flex-grow">
					<div class="btns">
						<input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="완료">
						<input onclick="history.back();" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소">
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%> 
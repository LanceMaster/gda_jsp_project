<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title><c:choose>
		<c:when test="${not empty inquiry}">문의글 수정</c:when>
		<c:otherwise>문의글 작성</c:otherwise>
	</c:choose></title>

<link rel="stylesheet"
	href="<c:url value='/static/css/inquiryWrite.css' />">

<!-- Summernote CSS -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="inquiry-write-container">
		<h2>
			<c:choose>
				<c:when test="${not empty inquiry}">문의글 수정</c:when>
				<c:otherwise>문의글 작성</c:otherwise>
			</c:choose>
		</h2>

		<form method="post" action="<c:url value='/lecture/inquiry/write' />"
			id="inquiryForm">
			<!-- 수정 시 전달할 ID -->
			<input type="hidden" name="lectureId"
				value="${inquiry.lectureId != null ? inquiry.lectureId : lectureId}" />

			<c:if test="${not empty inquiry}">
				<input type="hidden" name="editId" value="${inquiry.inquiryId}" />
			</c:if>

			<label for="title">제목</label> <input type="text" name="title"
				id="title" required
				value="${inquiry.title != null ? inquiry.title : ''}" /> <label
				for="content">문의 내용</label>
			<textarea name="content" id="content" required>${inquiry.content != null ? inquiry.content : ''}</textarea>

			<%-- <div class="form-actions">
				<a
					href="<c:url value='/lecture/inquiry/detail?inquiryId=${inquiry.inquiryId != null ? inquiry.inquiryId : lectureId}' />"
					class="cancel-btn">취소</a>
					
					
				<button type="submit">
					<c:choose>
						<c:when test="${not empty inquiry}">수정</c:when>
						<c:otherwise>제출</c:otherwise>
					</c:choose>
				</button>
			</div> --%>

			<div class="form-actions">
				<c:choose>
					<c:when test="${not empty inquiry}">
						<!-- 수정일 때: 상세 페이지로 이동 -->
						<a
							href="<c:url value='/lecture/inquiry/detail?inquiryId=${inquiry.inquiryId}' />"
							class="cancel-btn">취소</a>
					</c:when>
					<c:otherwise>
						<!-- 작성일 때: 예시로 강의 상세 페이지로 이동 -->
						<a href="<c:url value='/lecture/inquiry/list?lectureId=${lectureId}' />"
							class="cancel-btn">취소</a>
					</c:otherwise>
				</c:choose>

				<button type="submit">
					<c:choose>
						<c:when test="${not empty inquiry}">수정</c:when>
						<c:otherwise>제출</c:otherwise>
					</c:choose>
				</button>
			</div>
		</form>
	</div>

	<!-- Summernote JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#content').summernote({
				height : 300,
				placeholder : '문의 내용을 작성해주세요'
			});
		});
	</script>
</body>
</html>

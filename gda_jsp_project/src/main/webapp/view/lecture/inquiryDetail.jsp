<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<meta charset="UTF-8" />
<title>문의 상세</title>
<link rel="stylesheet"
	href="<c:url value='/static/css/inquiryDetail.css' />" />

<div class="inquiry-detail-container">
	<h2>문의 상세</h2>

	<div class="inquiry-box">
		<h3>${inquiry.title}</h3>
		<p class="inquiry-meta">작성자: ${inquiry.userName} | 작성일:
			${inquiry.createdAt.toString().replace("T", " ").substring(0, 16)}</p>
		<div class="inquiry-content">
			<pre>${inquiry.content}</pre>
		</div>
	</div>
	<div class="inquiry-actions">
		<a
			href="<c:url value='/lecture/inquiry/list?lectureId=${inquiry.lectureId}' />">←
			목록으로</a>
		<c:if test="${sessionScope.user.userId eq inquiry.userId}">
			<a
				href="<c:url value='/lecture/inquiry/write?lectureId=${inquiry.lectureId}&editId=${inquiry.inquiryId}' />">수정</a>
			<form method="post"
				action="<c:url value='/lecture/inquiry/delete?lectureId=${inquiry.lectureId}' />"
				onsubmit="return confirm('정말 삭제하시겠습니까?')">
				<input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />
				<input type="hidden" name="lectureId" value="${inquiry.lectureId}" />
				<button type="submit">삭제</button>
			</form>
		</c:if>
	</div>
</div>
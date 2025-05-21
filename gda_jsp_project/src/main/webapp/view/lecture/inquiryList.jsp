<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<meta charset="UTF-8">
<title>강의 문의 게시판</title>
<link rel="stylesheet" href="<c:url value='/static/css/inquiry.css' />">

<div class="inquiry-container">

	<!-- 🔍 검색 및 작성 버튼 -->
	<div class="inquiry-search">
		<input type="text" placeholder="강의명, 강사명 검색 (기능 미구현)">
		 <a
			href="${pageContext.request.contextPath}/lecture/inquiry/inquirywrite?lectureId=${lectureId}"
			class="write-btn"> 문의글 작성하기 </a>
	</div>


	<!-- 📋 문의글 목록 테이블 -->
	<table class="inquiry-table">
		<thead>
			<tr>
				<th>번호</th>
				<th>강의명</th>
				<th>문의 제목</th>
				<th>작성일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="inquiry" items="${inquiryList}" varStatus="status">
				<tr class="clickable-row"
					data-href="<c:url value='/lecture/inquiry/detail?inquiryId=${inquiry.inquiryId}' />">
					<td><c:out value="${status.index + 1}" /></td>
					<td><c:choose>
							<c:when test="${not empty inquiry.lectureTitle}">
								<c:out value="${inquiry.lectureTitle}" />
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose></td>
					<td><c:out value="${inquiry.title}" /></td>
					<td>${inquiry.createdAt.toString().replace("T", " ")}</td>
				</tr>
			</c:forEach>

			<c:if test="${empty inquiryList}">
				<tr>
					<td colspan="4" style="text-align: center;">등록된 문의가 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>

	<c:if test="${not empty sessionScope.msg}">
		<script>alert("${sessionScope.msg}");</script>
		<c:remove var="msg" scope="session" />
	</c:if>


	<script>
	// 클릭 가능한 row
	document.querySelectorAll('.clickable-row').forEach(row => {
		row.addEventListener('click', () => {
			window.location.href = row.dataset.href;
		});
	});
</script>

	<!-- 🔢 페이징 -->
	<div class="pagination">
		<c:if test="${currentPage > 1}">
			<%-- <a href="?page=${currentPage - 1}" class="page-btn">&lt; 이전</a>
			 --%>
			<a href="?page=${currentPage - 1}&lectureId=${lectureId}"
				class="page-btn">&lt; 이전</a>

		</c:if>

		<%-- <c:forEach begin="1" end="${totalPages}" var="pageNum">
			<a href="?page=${pageNum}"
				class="page-btn <c:if test='${pageNum == currentPage}'>active</c:if>">
				${pageNum} </a>
		</c:forEach> --%>

		<c:forEach begin="1" end="${totalPages}" var="pageNum">
			<a href="?page=${pageNum}&lectureId=${lectureId}"
				class="page-btn <c:if test='${pageNum == currentPage}'>active</c:if>">
				${pageNum} </a>
		</c:forEach>


		<c:if test="${currentPage < totalPages}">
			<%-- 
			<a href="?page=${currentPage + 1}" class="page-btn">다음 &gt;</a> --%>
			<a href="?page=${currentPage + 1}&lectureId=${lectureId}"
				class="page-btn">다음 &gt;</a>

		</c:if>
	</div>

</div>

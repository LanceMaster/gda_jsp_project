<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>강의 문의 게시판</title>
  <link rel="stylesheet" href="<c:url value='/static/css/inquiry.css' />">
</head>
<body>
<div class="inquiry-container">

  <!-- 🔍 검색 & 작성 -->
  <div class="inquiry-search">
    <input type="text" placeholder="상세하고 싶은 강의, 강사명을 입력해주세요">
    <a href="/lecture/inquiry/write" class="write-btn">문의글 작성하기</a>
  </div>

  <!-- 📋 문의글 테이블 -->
  <table class="inquiry-table">
    <thead>
      <tr>
        <th>게시 글 번호</th>
        <th>강의명</th>
        <th>문의 제목</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
  <c:forEach var="inquiry" items="${inquiryList}" varStatus="status">
    <tr>
      <td><c:out value='${status.index + 1}'/></td>
      <td><c:out value='${inquiry.lectureTitle}'/></td>

      <!-- 🔄 제목 클릭 시 상세 페이지로 이동 -->
      <td>
        <a href="<c:url value='/lecture/inquiry/detail?inquiryId=${inquiry.inquiryId}' />">
          <c:out value='${inquiry.title}'/>
        </a>
      </td>

      <td>
        <a href="/lecture/inquiry/detail?inquiryId=${inquiry.inquiryId}" class="detail-btn">상세보기</a>

        <form method="post"
              action="<c:url value='/lecture/inquiry/delete'/>"
              onsubmit="return confirm('정말 삭제하시겠습니까?')"
              style="display:inline;">
          <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />
          <input type="hidden" name="lectureId" value="${inquiry.lectureId}" />
          <button type="submit" class="delete-button">삭제</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</tbody>

  </table>

  <!-- 🔢 페이지네이션 -->
  <div class="pagination">
    <c:if test="${currentPage > 1}">
      <a href="?page=${currentPage - 1}" class="page-btn">&lt; 이전 페이지</a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="pageNum">
      <a href="?page=${pageNum}" class="page-btn <c:if test='${currentPage == pageNum}'>active</c:if>'">
        ${pageNum}
      </a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
      <a href="?page=${currentPage + 1}" class="page-btn">다음 페이지 &gt;</a>
    </c:if>
  </div>
</div>

<script>
  function confirmDelete() {
    return confirm("정말로 이 문의글을 삭제하시겠습니까?");
  }
</script>

</body>
</html>

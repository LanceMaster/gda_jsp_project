<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>문의글 수정</title>
  <link rel="stylesheet" href="<c:url value='/static/css/inquiry.css'/>" />
</head>
<body>
<div class="inquiry-container">
  <h2>📌 문의글 수정</h2>

  <!-- 수정 오류 메시지 -->
  <c:if test="${not empty error}">
    <div class="error-message">${error}</div>
  </c:if>

  <!-- 문의 수정 폼 -->
  <form method="post" action="<c:url value='/lecture/inquiry/edit'/>">
    <!-- 📌 숨겨진 필드: 문의 ID + 강의 ID -->
    <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />
    <input type="hidden" name="lectureId" value="${inquiry.lectureId}" />

    <!-- 제목 -->
    <label for="title">제목</label>
    <input type="text" id="title" name="title" value="${inquiry.title}" required />

    <!-- 내용 -->
    <label for="content">내용</label>
    <textarea id="content" name="content" rows="8" required>${inquiry.content}</textarea>

    <!-- 버튼 -->
    <div class="form-actions">
      <button type="submit">수정 완료</button>
      <a href="<c:url value='/lecture/inquiries?lectureId=${inquiry.lectureId}'/>" class="cancel-button">취소</a>
    </div>
  </form>
</div>
</body>
</html>

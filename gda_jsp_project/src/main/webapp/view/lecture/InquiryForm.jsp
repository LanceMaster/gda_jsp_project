<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>문의 작성</title>
  <link rel="stylesheet" href="<c:url value='/static/css/inquiry.css' />">
</head>
<body>
<div class="inquiry-container">
  <h2>강의 문의 작성</h2>

  <form method="post" action="<c:url value='/lecture/inquiry/write' />">
    <input type="hidden" name="lectureId" value="${lectureId}" />

    <!-- 제목 -->
    <div class="form-group">
      <label for="title">제목</label>
      <input type="text" name="title" id="title" required placeholder="제목을 입력해주세요">
    </div>

    <!-- 내용 -->
    <div class="form-group">
      <label for="content">내용</label>
      <textarea name="content" id="content" rows="6" required placeholder="문의할 내용을 입력해주세요."></textarea>
    </div>

    <!-- 에러 메시지 -->
    <c:if test="${not empty error}">
      <p class="error-msg">${error}</p>
    </c:if>

    <!-- 버튼 -->
    <div class="form-actions">
      <button type="submit" class="submit-btn">등록</button>
      <a href="<c:url value='/lecture/inquiries?lectureId=${lectureId}' />" class="cancel-btn">취소</a>
    </div>
  </form>
</div>
</body>
</html>

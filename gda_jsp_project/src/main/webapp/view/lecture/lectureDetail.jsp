<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${lecture.title} - 강의 상세</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lectureDetail.css' />" />
  <script src="<c:url value='/static/js/lecture_detail.js' />" defer></script>
</head>
<body>
<div class="lecture-container">

  <!-- 썸네일 -->
  <div class="lecture-thumbnail">
    <img src="<c:out value='${lecture.thumbnail != null ? lecture.thumbnail : "/static/images/default.png"}'/>" alt="강의 썸네일" />
  </div>

  <!-- 타이틀 + 평점 -->
  <div class="lecture-header">
    <div class="header-left">
      <h1><c:out value="${lecture.title}" /></h1>
      <p class="lecture-sub"><c:out value="${lecture.category}" /></p>
    </div>
    <div class="header-right">
      <p class="lecture-price">
        <strong>
          ₩<fmt:formatNumber value="${lecture.price != null ? lecture.price : 0}" type="number" />
        </strong>
      </p>
      <button class="start-button">지금 시작하기</button>
      <p class="lecture-rating">
        ⭐ <c:out value="${lecture.avgRating != null ? lecture.avgRating : 0}" /> / 5.0
      </p>
    </div>
  </div>

  <!-- 설명 -->
  <div class="lecture-description">
    <h2>강의 설명</h2>
    <p><c:out value="${lecture.description}" /></p>
    <ul>
      <li><span class="icon">🟦</span> Spring Boot</li>
      <li><span class="icon">🟪</span> JPA</li>
      <li><span class="icon">🟫</span> MySQL</li>
      <li><span class="icon">🟦</span> 배포: AWS 등</li>
    </ul>
    <button class="ask-button">질문 게시판 바로가기</button>
  </div>

  <!-- 리뷰 등록 폼 -->
  <div class="review-form">
    <h3><c:out value="${lecture.title}" /> 수강 후기 작성</h3>
    <textarea id="reviewContent" placeholder="댓글을 입력해 주세요."></textarea>
    <select id="rating">
      <option value="5">⭐⭐⭐⭐⭐</option>
      <option value="4">⭐⭐⭐⭐</option>
      <option value="3">⭐⭐⭐</option>
      <option value="2">⭐⭐</option>
      <option value="1">⭐</option>
    </select>
    <button id="submitReview">제출</button>
  </div>

  <!-- 기존 리뷰 출력 (Ajax 없을 때 대체용) -->
  <ul class="review-list">
    <c:forEach var="review" items="${reviewList}">
      <li>
        <strong><c:out value="${review.reviewer}" /></strong>
        ⭐ <c:out value="${review.rating}" /> / 5.0
        <p><c:out value="${review.content}" /></p>
      </li>
    </c:forEach>
  </ul>

</div>
</body>
</html>

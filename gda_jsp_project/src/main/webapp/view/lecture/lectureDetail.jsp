<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>${lecture.title} - 강의 상세</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lectureDetail.css' />">
</head>
<body>
<div class="lecture-container">

  <!-- 썸네일 -->
  <c:choose>
    <c:when test="${not empty lecture.thumbnail}">
      <c:url var="thumbnailUrl" value="${lecture.thumbnail}" />
    </c:when>
    <c:otherwise>
      <c:url var="thumbnailUrl" value="/static/images/default.png" />
    </c:otherwise>
  </c:choose>

  <div class="lecture-top">
    <div class="lecture-image">
      <img src="${thumbnailUrl}" alt="${lecture.title}" />
    </div>
    <div class="lecture-info">
      <h1>${lecture.title}</h1>
      <p class="lecture-category">"${lecture.category} - 배포까지 한 번에"</p>
      <div class="price-rating">
        <p class="lecture-price">₩<fmt:formatNumber value="${lecture.price}" type="number" /></p>
        <button class="start-btn">지금 시작하기</button>
        <span class="lecture-rating">⭐ ${lecture.avgRating} / 5.0</span>
      </div>
    </div>
  </div>

  <div class="lecture-description">
    <h2>강의 설명</h2>
    <p class="desc-subtitle">키워드는: 왜 이 강의인가?</p>
    <p class="desc-main">${lecture.description}</p>
    <ul class="tech-list">
      <li>🟦 Spring Boot: 빠르고 강력한 웹 애플리케이션 프레임워크</li>
      <li>🟪 JPA: 객체지향적 데이터베이스 다루기</li>
      <li>🟫 MySQL: 가장 많이 쓰이는 RDBMS</li>
      <li>🟦 배포: 다양한 클라우드 서비스 (AWS, Heroku 등)</li>
    </ul>
    <p class="desc-footer">이 수업은 실전형 프로젝트를 통해 핵심 개념을 빠르게 학습할 수 있도록 설계되어 있습니다.</p>

    <!-- 질문 게시판 바로가기 -->
    <a href="<c:url value='/lecture/inquiryList' />" class="ask-btn">
      질문 게시판 바로가기
    </a>
  </div>

  <div class="review-section">
    <div class="review-header">
      <h3>${lecture.title}</h3>
      <span class="lecture-rating">⭐ ${lecture.avgRating} / 5.0</span>
    </div>

    <div class="review-form">
      <textarea placeholder="댓글을 입력해 주세요."></textarea>
      <div class="review-controls">
        <select>
          <option value="5">⭐⭐⭐⭐⭐</option>
          <option value="4">⭐⭐⭐⭐</option>
          <option value="3">⭐⭐⭐</option>
          <option value="2">⭐⭐</option>
          <option value="1">⭐</option>
        </select>
        <button class="submit-btn">제출</button>
      </div>
    </div>

    <ul class="review-list">
      <c:forEach var="review" items="${reviewList}">
        <li class="review-item">
          <div class="review-card">

            <!-- 👤 유저 정보 -->
            <div class="review-user">
              <img src="<c:url value='/static/images/user.png' />" class="user-icon" />
              <div class="user-meta">
                <div class="meta-top">
                  <strong class="reviewer-name">${review.reviewer}</strong>
                  <span class="review-date">${review.formattedCreatedAt}</span>
                </div>
                <div class="review-stars">
                  <c:forEach begin="1" end="5" var="i">
                    <c:choose>
                      <c:when test="${i <= review.rating}">⭐</c:when>
                      <c:otherwise>☆</c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>
              </div>
            </div>

            <!-- 🧾 리뷰 내용 -->
            <p class="review-content">${review.content}</p>
          </div>
        </li>
      </c:forEach>
    </ul>
  </div>

</div>
</body>
</html>

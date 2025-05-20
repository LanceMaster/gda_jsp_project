
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
<title>${lecture.title}-강의 상세</title>
<link rel="stylesheet"
	href="<c:url value='/static/css/lectureDetail.css' />">

<!-- 상단에 JSTL URL 정의 -->
<c:url var="addCartUrl" value="/cart/add" />
<c:url var="cartPageUrl" value="/lecture/cart" />
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
        <p class="lecture-price">₩${lecture.price}<fmt:formatNumber value="${lecture.price}" type="number" /></p>
    <button class="start-btn" onclick="startNow(${lecture.lectureId})">지금 시작하기</button>
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
    <a href="<c:url value='/lecture/inquiry/list' />">강의 질문 게시판</a>
  </div>

  <div class="review-section">
    <div class="review-header">
      <h3>${lecture.title}</h3>
      <span class="lecture-rating">⭐ ${lecture.avgRating} / 5.0</span>
    </div>

<c:if test="${canReview}">
  <form id="reviewForm" class="review-form" method="post" autocomplete="off">
    <input type="hidden" name="lectureId" value="${lecture.lectureId}" />
    
    <label for="reviewContent" class="visually-hidden">리뷰 내용</label>
    <textarea id="reviewContent" name="content" placeholder="댓글을 입력해 주세요." maxlength="1000" required></textarea>
    
    <div class="review-controls">
      <label for="reviewRating" class="visually-hidden">평점</label>
      <select id="reviewRating" name="rating" required>
        <option value="">⭐ 평점을 선택하세요</option>
        <option value="5">⭐⭐⭐⭐⭐</option>
        <option value="4">⭐⭐⭐⭐</option>
        <option value="3">⭐⭐⭐</option>
        <option value="2">⭐⭐</option>
        <option value="1">⭐</option>
      </select>
      <button type="submit" class="submit-btn" id="reviewSubmitBtn">제출</button>
    </div>
    <div id="reviewErrorMsg" class="form-error" style="display:none;"></div>
    <div id="reviewSuccessMsg" class="form-success" style="display:none;"></div>
  </form>
</c:if>


<!-- ✅ 리뷰 작성 불가 조건 안내 -->
<c:if test="${not canReview}">
  <c:choose>
    <c:when test="${not hasEnrolled}">
      <p class="review-guide">※ 리뷰를 작성하려면 강의를 수강 중이어야 합니다.</p>
    </c:when>
    <c:when test="${hasReviewed}">
      <p class="review-guide">※ 이미 리뷰를 작성하셨습니다.</p>
    </c:when>
    <c:otherwise>
      <p class="review-guide">※ 리뷰 작성 조건이 충족되지 않았습니다.</p>
    </c:otherwise>
  </c:choose>
</c:if>


<c:if test="${not empty reviewList}">
  <p style="font-size:15px;color:#666;margin-bottom:8px;">
    총 <strong>${fn:length(reviewList)}</strong>개의 리뷰가 등록되었습니다.
  </p>
</c:if>
<c:if test="${empty reviewList}">
  <p style="font-size:15px;color:#999;margin-bottom:8px;">
    아직 리뷰가 없습니다. 첫 리뷰를 작성해보세요!
  </p>
</c:if>


<!-- ✅ 리뷰 목록 -->
<ul class="review-list" id="reviewList">
  <c:forEach var="review" items="${reviewList}">
    <li class="review-item">
      <div class="review-card">
        <div class="review-user">
          <img src="<c:url value='/static/images/user.png' />" class="user-icon" />
          <div class="user-meta">
            <div class="meta-top">
              <strong class="reviewer-name">${review.reviewer}</strong>
              <span class="review-date">
                <fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd HH:mm" />
              </span>
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
        <p class="review-content">${review.content}</p>
      </div>
    </li>
  </c:forEach>
</ul>

</div>
<script>

  const addCartUrl = '${addCartUrl}';
  const cartPageUrl = '${cartPageUrl}';

  function startNow(lectureId) {
    fetch(addCartUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ lectureId: lectureId })
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert("✅ 장바구니에 담겼습니다. 장바구니로 이동합니다.");
        location.href = cartPageUrl;
      } else {
        alert("❌ 실패: " + data.message);
      }
    })
    .catch(err => {
      console.error("🚨 오류:", err);
      alert("서버 통신 중 오류 발생");
    });
  }
  
  
  // ✅ 리뷰 제출 UX 개선 (폼 유효성 + 메시지)
  const reviewForm = document.getElementById("reviewForm");

  if (reviewForm) {
    reviewForm.addEventListener("submit", function(e) {
      const rating = reviewForm.rating.value;
      const content = reviewForm.content.value.trim();

      // 에러/성공 메시지 DOM 없으면 생성
      let errorDiv = document.getElementById("reviewErrorMsg");
      let successDiv = document.getElementById("reviewSuccessMsg");
      if (!errorDiv) {
        errorDiv = document.createElement("div");
        errorDiv.id = "reviewErrorMsg";
        errorDiv.className = "form-error";
        reviewForm.appendChild(errorDiv);
      }
      if (!successDiv) {
        successDiv = document.createElement("div");
        successDiv.id = "reviewSuccessMsg";
        successDiv.className = "form-success";
        reviewForm.appendChild(successDiv);
      }

      // 초기화
      errorDiv.style.display = "none";
      successDiv.style.display = "none";

      // 클라이언트 유효성 검증
      if (!rating || !content) {
        e.preventDefault();
        errorDiv.innerText = "⚠️ 평점과 내용을 모두 입력하세요.";
        errorDiv.style.display = "block";
        return;
      }

      if (content.length > 1000) {
        e.preventDefault();
        errorDiv.innerText = "⚠️ 리뷰는 1000자 이내로 작성해주세요.";
        errorDiv.style.display = "block";
        return;
      }

      // UX 피드백 (옵션)
      successDiv.innerText = "리뷰를 등록 중입니다...";
      successDiv.style.display = "block";
    });
  }
  </script>
</body>
</html>

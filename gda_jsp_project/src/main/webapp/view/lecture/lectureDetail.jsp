
<%@page import="model.dto.LectureDTO"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<html>
<head>
<title>${lecture.title}-강의상세</title>
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
					<p class="lecture-price">
						₩${lecture.price}
						<fmt:formatNumber value="${lecture.price}" type="number" />
					</p>
					<button class="start-btn" onclick="startNow(${lecture.lectureId})">지금
						시작하기</button>
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
<div class="modern-action-wrap">
	<a
				href="${pageContext.request.contextPath}/lecture/inquiry/list?lectureId=${lecture.lectureId}"
				class="go-inquiry-btn"> 💬 강의 질문 게시판 바로가기 </a>
</div>
  </div>

  <div class="review-section">
    <div class="review-header">
      <h3>${lecture.title}</h3>
      <span class="lecture-rating">⭐ ${lecture.avgRating} / 5.0</span>
    </div>

<!-- ✅ 리뷰 작성 가능 조건 -->
<c:if test="${canReview}">
<form id="reviewForm" method="post" action="${pageContext.request.contextPath}/review" class="review-form">
  <input type="hidden" name="lectureId" value="${lecture.lectureId}" />

  <!-- 작성자명 + 평점 -->
<div class="review-header-line modern">
  <input type="text" id="reviewTitle" name="title" class="review-nickname" placeholder="닉네임을 입력하세요" required />
  <select name="rating" class="rating-select" required>
    <option value="" disabled selected hidden>평점 선택</option>
    <option value="5">⭐⭐⭐⭐⭐</option>
    <option value="4">⭐⭐⭐⭐</option>
    <option value="3">⭐⭐⭐</option>
    <option value="2">⭐⭐</option>
    <option value="1">⭐</option>
  </select>
</div>

<!-- 리뷰 입력 + 제출 버튼 우측 하단 배치 -->
<div class="review-content-wrap">
  <textarea id="reviewContent" name="content" rows="4" placeholder="리뷰를 남겨주세요." required></textarea>
  <div class="review-submit-right">
    <button type="submit" class="submit-btn">제출</button>
  </div>
</div>

  <!-- 메시지 -->
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
				<p style="font-size: 15px; color: #666; margin-bottom: 8px;">
					총 <strong>${fn:length(reviewList)}</strong>개의 리뷰가 등록되었습니다.
				</p>
			</c:if>
			<c:if test="${empty reviewList}">
				<p style="font-size: 15px; color: #999; margin-bottom: 8px;">아직
					리뷰가 없습니다. 첫 리뷰를 작성해보세요!</p>
			</c:if>


			<!-- ✅ 리뷰 목록 -->
			<ul class="review-list" id="reviewList">
				<c:forEach var="review" items="${reviewList}">
					<li class="review-item">
						<div class="review-card">
							<div class="review-user">
								<img src="<c:url value='/static/images/user.png' />"
									class="user-icon" />
								<div class="user-meta">
									<div class="meta-top">
										<strong class="reviewer-name">${review.reviewer}</strong> <span
											class="review-date"> <fmt:formatDate
												value="${review.createdAt}" pattern="yyyy-MM-dd HH:mm" />
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
  
  const reviewForm = document.getElementById("reviewForm");

  if (reviewForm) {
    reviewForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const rating = reviewForm.rating.value;
      const title = reviewForm.title.value.trim();
      const content = reviewForm.content.value.trim();

      const errorBox = document.getElementById("reviewErrorMsg");
      const successBox = document.getElementById("reviewSuccessMsg");

      errorBox.style.display = "none";
      successBox.style.display = "none";

      if (!rating || !title || !content) {
        errorBox.innerText = "⚠ 모든 필드를 입력해 주세요.";
        errorBox.style.display = "block";
        return;
      }

      const params = new URLSearchParams(new FormData(reviewForm));

      fetch(reviewForm.action, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: params,
      })
        .then((res) => res.json())
        .then((data) => {
          if (data.success) {
            successBox.innerText = "✅ 리뷰가 성공적으로 등록되었습니다.";
            successBox.style.display = "block";

            // 폼 초기화
            reviewForm.reset();

            // 일정 시간 후 페이지 리로드 또는 이동
            setTimeout(() => {
              window.location.href = data.redirectUrl;
            }, 1000);
          } else {
            errorBox.innerText = data.message || "❌ 리뷰 등록 실패";
            errorBox.style.display = "block";
          }
        })
        .catch((error) => {
          console.error("🚨 서버 오류:", error);
          errorBox.innerText = "서버 오류가 발생했습니다. 다시 시도해 주세요.";
          errorBox.style.display = "block";
        });
    });
  }


  
  </script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="<c:url value='/static/css/lecture.css' />" />
<!-- ⭐ 별점 + 댓글 입력 영역 -->
<div class="review-box">
  <h2>
    실전 프로젝트로 배우는 백엔드 API 개발
    <span class="average-rating">⭐ 4.9 / 5.0</span>
  </h2>

  <div class="review-input">
    <textarea id="reviewContent" placeholder="댓글을 입력하세요."></textarea>

    <div class="star-rating" id="starRating">
      <span data-value="1">⭐</span>
      <span data-value="2">⭐</span>
      <span data-value="3">⭐</span>
      <span data-value="4">⭐</span>
      <span data-value="5">⭐</span>
    </div>

    <input type="hidden" id="reviewRating" value="5" />
    <button id="submitReview">제출</button>
  </div>

  <!-- ⭐ 댓글 리스트 출력 영역 -->
  <ul id="reviewList" class="review-list"></ul>
</div>

<script>
  // ✅ JSP에서 lectureId 안전하게 출력
  const lectureId = ${lecture.lectureId};

  // ⭐ 별점 선택 UI 처리
  const stars = document.querySelectorAll("#starRating span");
  const ratingInput = document.getElementById("reviewRating");

  stars.forEach(star => {
    star.addEventListener("click", () => {
      const selected = parseInt(star.dataset.value);
      ratingInput.value = selected;

      stars.forEach(s => s.classList.remove("active"));
      for (let i = 0; i < selected; i++) {
        stars[i].classList.add("active");
      }
    });
  });

  // 📥 댓글 제출
  document.getElementById("submitReview").addEventListener("click", () => {
    const content = document.getElementById("reviewContent").value.trim();
    const rating = ratingInput.value;

    if (!content) {
      alert("댓글을 입력해주세요.");
      return;
    }

    // 💡 EL 충돌 방지를 위해 문자열 + encodeURIComponent로 구성
    const bodyData = "lectureId=" + lectureId +
                     "&content=" + encodeURIComponent(content) +
                     "&rating=" + rating;

    fetch("/review", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: bodyData
    })
    .then(() => {
      loadReviews();
      document.getElementById("reviewContent").value = "";
      stars.forEach(s => s.classList.remove("active"));
      for (let i = 0; i < rating; i++) {
        stars[i].classList.add("active");
      }
    });
  });

  // 📤 댓글 목록 불러오기
  function loadReviews() {
    fetch(`/review?lectureId=` + lectureId)
      .then(res => res.json())
      .then(data => {
        const listHtml = data.map(r => {
          const dateStr = new Date(r.createdAt).toLocaleDateString("ko-KR");
          return `
            <li>
              <strong>${r.userName}</strong> ⭐ ${r.rating}/5<br/>
              ${r.content}<br/>
              <small>${dateStr}</small>
            </li>
          `;
        }).join("");
        document.getElementById("reviewList").innerHTML = listHtml;
      });
  }

  // 🟡 페이지 로드시 자동 리뷰 로딩
  loadReviews();
</script>

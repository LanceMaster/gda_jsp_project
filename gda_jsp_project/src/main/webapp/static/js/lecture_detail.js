// 📁 /static/js/lecture_detail.js

document.addEventListener("DOMContentLoaded", () => {
  const lectureId = new URLSearchParams(window.location.search).get("lectureId");
  const reviewList = document.getElementById("reviewList");
  const submitBtn = document.getElementById("submitReview");

  // ✅ 리뷰 목록 불러오기
  function loadReviews() {
    fetch(`/review?lectureId=${lectureId}`)
      .then((res) => res.json())
      .then((data) => {
        reviewList.innerHTML = "";
        data.forEach((review) => {
          const li = document.createElement("li");
          li.className = "review-item";
          li.innerHTML = `
            <div class="review-author">👤 ${review.userName || "익명"}</div>
            <div class="review-rating">⭐ ${review.rating}</div>
            <div class="review-content">${review.content}</div>
            <div class="review-date">🕒 ${review.createdAt || "-"}</div>
          `;
          reviewList.appendChild(li);
        });
      });
  }

  // ✅ 리뷰 제출하기
  submitBtn.addEventListener("click", () => {
    const content = document.getElementById("reviewContent").value;
    const rating = document.getElementById("rating").value;

    if (!content.trim()) {
      alert("리뷰 내용을 입력해 주세요.");
      return;
    }

    fetch("/review", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `lectureId=${lectureId}&content=${encodeURIComponent(content)}&rating=${rating}`
    })
      .then(res => {
        if (!res.ok) throw new Error("등록 실패");
        return res.json();
      })
      .then(() => {
        document.getElementById("reviewContent").value = "";
        document.getElementById("rating").value = "5";
        loadReviews();
      })
      .catch(err => alert("리뷰 등록 중 오류 발생: " + err.message));
  });

  // ✅ 첫 로딩 시 리뷰 목록 출력
  loadReviews();
});
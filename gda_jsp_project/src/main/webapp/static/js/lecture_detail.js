document.addEventListener("DOMContentLoaded", () => {
  const reviewForm = document.getElementById("reviewForm");
  const reviewList = document.getElementById("reviewList");
  const lectureId = new URLSearchParams(window.location.search).get("lectureId");

  // ✅ 리뷰 목록 출력 함수
  function loadReviews() {
    fetch(`/review?lectureId=${lectureId}`)
      .then(res => res.json())
      .then(data => {
        reviewList.innerHTML = "";
        data.forEach(review => {
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
      })
      .catch(err => {
        console.error("리뷰 목록 불러오기 실패:", err);
        reviewList.innerHTML = "<li>리뷰를 불러오는 중 오류가 발생했습니다.</li>";
      });
  }

  // ✅ 리뷰 제출 이벤트 바인딩
  if (reviewForm) {
    reviewForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const content = reviewForm.content.value.trim();
      const rating = reviewForm.rating.value;

      if (!content) {
        alert("리뷰 내용을 입력해 주세요.");
        return;
      }

      const data = {
        lectureId: lectureId,
        content: content,
        rating: rating
      };

      fetch("/lecture/review/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      })
        .then(res => res.json())
        .then(json => {
          if (json.success) {
            alert("리뷰가 등록되었습니다.");
            reviewForm.reset();
            loadReviews();
          } else {
            alert("리뷰 등록 실패: " + json.message);
          }
        })
        .catch(err => {
          console.error("리뷰 등록 중 오류:", err);
          alert("서버 오류가 발생했습니다.");
        });
    });
  }

  // ✅ 초기 리뷰 목록 로딩
  loadReviews();
});

// ✅ 장바구니 추가 함수 (강의 카드용)
function addToCart(lectureId) {
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
      showSnackbar("✅ 장바구니에 담겼습니다!");
    } else {
      showSnackbar("❌ 실패: " + data.message);
    }
  })
  .catch(err => {
    console.error("🚨 오류:", err);
    showSnackbar("서버 통신 중 오류 발생");
  });
}

// ✅ 페이지 로드 후 DOM 준비 이벤트
document.addEventListener("DOMContentLoaded", function() {
  // 🔹 [1] 검색 입력 빈값 검사 및 UX 피드백
  var searchInput = document.querySelector(".search-box input[type='text']");
  if (searchInput) {
    searchInput.addEventListener("keydown", function(e) {
      if (e.key === "Enter") {
        var value = searchInput.value.trim();
        if (!value) {
          e.preventDefault();
          searchInput.classList.add('search-error');
          showSnackbar('검색어를 입력해주세요.');
          setTimeout(() => searchInput.classList.remove('search-error'), 900);
        }
      }
    });
    searchInput.addEventListener("input", function() {
      if (searchInput.classList.contains('search-error')) {
        searchInput.classList.remove('search-error');
      }
    });
  }

  // 🔹 [2] 중복 방지 버튼 비활성화 (추후 삭제 기능 도입 시)
  document.querySelectorAll(".delete-button").forEach(function(btn) {
    btn.addEventListener("click", function() {
      if (btn.disabled) return false;
      btn.disabled = true;
      setTimeout(() => btn.disabled = false, 3000);
    });
  });

  // 🔹 [3] 여러 버튼 중복 클릭 방지
  ["add-cart-btn", "write-btn", "detail-btn"].forEach(function(cls) {
    document.querySelectorAll("." + cls).forEach(function(btn) {
      btn.addEventListener("click", function() {
        if (btn.disabled) return false;
        btn.disabled = true;
        setTimeout(() => btn.disabled = false, 2000);
      });
    });
  });

  // 🔹 [4] 테이블 행 클릭 UX (강의 목록은 카드 기반이라 생략 가능)
  // row click logic은 필요 시 다른 페이지에서 사용

});

// ✅ 공통 스낵바 메시지 함수 (전역 함수로 선언)
function showSnackbar(msg) {
  var bar = document.createElement("div");
  bar.innerText = msg;
  bar.style.position = "fixed";
  bar.style.bottom = "50px";
  bar.style.left = "50%";
  bar.style.transform = "translateX(-50%)";
  bar.style.background = "#222";
  bar.style.color = "#fff";
  bar.style.padding = "12px 32px";
  bar.style.borderRadius = "999px";
  bar.style.fontSize = "15px";
  bar.style.boxShadow = "0 2px 12px #0002";
  bar.style.zIndex = "9999";
  bar.style.opacity = "0.98";
  document.body.appendChild(bar);
  setTimeout(() => { bar.style.opacity = "0"; }, 1200);
  setTimeout(() => { bar.remove(); }, 1700);
}

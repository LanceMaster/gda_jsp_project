document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector(".filter-form");
  const searchInput = form.querySelector("input[name='keyword']");
  const sortSelect = form.querySelector("select[name='sort']");
  const searchBtn = form.querySelector(".search-btn");

  // ✅ 장바구니 추가 함수 (중복 방지 포함)
  window.addToCart = function (lectureId) {
    const button = document.querySelector(`.add-cart-btn[onclick*="${lectureId}"]`);
    if (button && button.disabled) return;

    if (button) {
      button.disabled = true;
      setTimeout(() => { button.disabled = false }, 2000);
    }

    fetch(addCartUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ lectureId })
    })
    .then(res => res.json())
    .then(data => {
      showSnackbar(data.success ? "✅ 장바구니에 담겼습니다!" : "❌ 실패: " + data.message);
    })
    .catch(err => {
      console.error("🚨 오류:", err);
      showSnackbar("서버 통신 중 오류 발생");
    });
  };

  // ✅ 공통 스낵바 함수
  function showSnackbar(msg) {
    const existing = document.querySelector(".snackbar");
    if (existing) existing.remove();

    const bar = document.createElement("div");
    bar.className = "snackbar";
    bar.innerText = msg;

    Object.assign(bar.style, {
      position: "fixed",
      bottom: "50px",
      left: "50%",
      transform: "translateX(-50%)",
      background: "#222",
      color: "#fff",
      padding: "12px 32px",
      borderRadius: "999px",
      fontSize: "15px",
      boxShadow: "0 2px 12px #0002",
      zIndex: "9999",
      opacity: "0.98",
      transition: "opacity 0.5s ease"
    });

    document.body.appendChild(bar);
    setTimeout(() => { bar.style.opacity = "0"; }, 1200);
    setTimeout(() => { bar.remove(); }, 1700);
  }

  // ✅ 검색 UX: Enter 입력 시 처리
  if (searchInput) {
    searchInput.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        const val = searchInput.value.trim();
        if (!val) {
          e.preventDefault();
          searchInput.classList.add("search-error");
          showSnackbar("검색어를 입력해주세요.");
          setTimeout(() => searchInput.classList.remove("search-error"), 900);
        } else {
          removeHiddenInput("category"); // 전체 범위 검색
        }
      }
    });

    searchInput.addEventListener("input", () => {
      searchInput.classList.remove("search-error");
    });
  }

  // ✅ 검색 버튼 클릭 시 category 제거
  if (searchBtn) {
    searchBtn.addEventListener("click", function () {
      const val = searchInput.value.trim();
      if (!val) {
        searchInput.classList.add("search-error");
        showSnackbar("검색어를 입력해주세요.");
        setTimeout(() => searchInput.classList.remove("search-error"), 900);
        return false;
      }

      form.querySelectorAll('input[name="category"]').forEach(el => el.remove());
    });
  }


  // ✅ 정렬 드롭다운 UX 개선
  if (sortSelect) {
    sortSelect.addEventListener("change", function () {
      const val = this.value;
      addOrUpdateHidden("sort", val);
      form.submit();
    });
  }

  // ✅ 카테고리 버튼 클릭 시: 검색어 초기화 + 필터 상태만 전송
  document.querySelectorAll(".category-btn").forEach(btn => {
    btn.addEventListener("click", function (e) {
      e.preventDefault();
      const categoryVal = this.value;

      // 기존 hidden input 제거
      form.querySelectorAll("input[type='hidden']").forEach(el => el.remove());

      // 검색어 초기화
      if (searchInput) searchInput.value = "";

      form.appendChild(createHidden("category", categoryVal));
      if (sortSelect) form.appendChild(createHidden("sort", sortSelect.value));
      form.appendChild(createHidden("page", "1")); // 페이지 초기화
      form.submit();
    });
  });

  // ✅ 다중 버튼 중복 클릭 방지 (장바구니 제외)
  ["write-btn", "detail-btn", "delete-button"].forEach(cls => {
    document.querySelectorAll("." + cls).forEach(btn => {
      btn.addEventListener("click", function () {
        if (btn.disabled) return;
        btn.disabled = true;
        setTimeout(() => btn.disabled = false, 2000);
      });
    });
  });

  // 🔧 헬퍼 함수들
  function createHidden(name, value) {
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = name;
    input.value = value;
    return input;
  }

  function addOrUpdateHidden(name, value) {
    let el = form.querySelector(`input[name="${name}"]`);
    if (!el) {
      el = createHidden(name, value);
      form.appendChild(el);
    } else {
      el.value = value;
    }
  }

  function removeHiddenInput(name) {
    const el = form.querySelector(`input[name="${name}"]`);
    if (el) el.remove();
  }
});

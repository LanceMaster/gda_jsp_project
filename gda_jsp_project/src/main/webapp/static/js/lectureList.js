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
      alert("✅ 장바구니에 담겼습니다!");
      // 예: 상세로 이동하고 싶다면
      // location.href = lectureDetailUrl + '?lectureId=' + lectureId;
    } else {
      alert("❌ 실패: " + data.message);
    }
  })
  .catch(err => {
    console.error("🚨 오류:", err);
    alert("서버 통신 중 오류 발생");
  });
}

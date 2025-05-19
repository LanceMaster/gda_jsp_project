function addToCart(lectureId) {
  fetch('/cart/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ lectureId })
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
      alert('장바구니에 추가되었습니다!');
    } else {
      alert('추가 실패: ' + data.message);
    }
  })
  .catch(err => {
    alert('오류 발생: ' + err);
  });
}

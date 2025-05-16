// ✅ 1. jQuery 비동기 리뷰 등록 및 목록 조회 스크립트

$(function () {
  const lectureId = $('input[name="lectureId"]').val();

  // ✅ 리뷰 등록 처리
  $('#reviewForm').on('submit', function (e) {
    e.preventDefault();

    $.ajax({
      type: 'POST',
      url: '/review',
      data: $(this).serialize(),
      success: function (res) {
        if (res.success) {
          alert('리뷰가 등록되었습니다!');
          $('#reviewForm')[0].reset();
          loadReviews();
        } else {
          alert(res.message || '리뷰 등록 실패');
        }
      },
      error: function (xhr) {
        if (xhr.status === 401) {
          alert('로그인이 필요합니다.');
        } else {
          alert('서버 오류로 리뷰 등록에 실패했습니다.');
        }
      }
    });
  });

  // ✅ 리뷰 목록 불러오기
  function loadReviews() {
    $.ajax({
      type: 'GET',
      url: '/review',
      data: { lectureId },
      success: function (list) {
        let html = '';

        for (const review of list) {
          const stars = '⭐'.repeat(review.rating) + '☆'.repeat(5 - review.rating);
          const createdAt = new Date(review.createdAt).toLocaleString();

          html += `
            <li class="review-item">
              <div class="review-card">
                <div class="review-user">
                  <img src="/static/images/user.png" class="user-icon" />
                  <div class="user-meta">
                    <div class="meta-top">
                      <strong class="reviewer-name">${review.reviewer}</strong>
                      <span class="review-date">${createdAt}</span>
                    </div>
                    <div class="review-stars">${stars}</div>
                  </div>
                </div>
                <p class="review-content">${review.content}</p>
              </div>
            </li>
          `;
        }

        $('#reviewList').html(html);
      },
      error: function () {
        $('#reviewList').html('<li>리뷰를 불러오지 못했습니다.</li>');
      }
    });
  }

  // ✅ 최초 실행 시 리뷰 목록 로딩
  loadReviews();
});

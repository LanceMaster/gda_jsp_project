<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

  <title>장바구니</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/cart.css" />
  <h2>🛒 신청하기 - Step 1. 강의 선택</h2>

  <form method="post" action="${pageContext.request.contextPath}/cart/checkout" class="cart-container">

    <div class="lecture-list">
 <c:choose>
  <c:when test="${not empty cartLectures}">
    <c:forEach var="lec" items="${cartLectures}">
      <div class="lecture-item">
        <label>
          <input type="checkbox" name="lectureIds" value="${lec.lectureId}" data-price="${lec.price}" checked />
      <img src="<c:url value='${lec.thumbnail}' />" alt="${lec.title}" class="lecture-thumbnail" />
          <div class="lecture-info">
            <div class="lecture-title">${lec.title}</div>
            <div class="lecture-price">
              <fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" />
            </div>
          </div>
        </label>
      </div>
    </c:forEach>
  </c:when>
  <c:otherwise>
    <div class="empty-cart">
      <p class="empty-cart-msg">장바구니에 담긴 강의가 없습니다.</p>
    </div>
  </c:otherwise>
</c:choose>
    </div>

<!-- 주문 요약 -->
<div class="summary-box">
  <h3>주문 요약</h3>
  <ul class="summary-list" id="summaryList"></ul>
  <div class="total-section">
    Total: ₩<span id="totalPrice">0</span>
  </div>
  <button type="submit" id="checkoutBtn" class="checkout-btn">결제하기</button>
</div>


  </form>
  
<script>
function updateCartSummary() {
	  let total = 0;
	  const summaryList = document.getElementById('summaryList');
	  summaryList.innerHTML = ''; // 기존 내용 초기화

	  document.querySelectorAll('input[name="lectureIds"]').forEach(cb => {
	    if (cb.checked) {
	      const lectureId = cb.value;
	      const price = parseInt(cb.dataset.price, 10);
	      const title = cb.closest('.lecture-item').querySelector('.lecture-title').textContent;

	      if (!isNaN(price)) {
	        total += price;

	        const li = document.createElement('li');
	        li.className = 'summary-item';
	        li.innerHTML = `[${lectureId}] ${title} - ₩${price.toLocaleString()}`;
	        summaryList.appendChild(li);
	      }
	    }
	  });

	  document.getElementById('totalPrice').textContent = total.toLocaleString();
	}


  function validateCheckout(e) {
    const checked = document.querySelectorAll('input[name="lectureIds"]:checked');
    if (checked.length === 0) {
      alert("📛 최소 하나의 강의를 선택해주세요.");
      e.preventDefault();
    }
  }

  // 초기 실행 및 바인딩
  document.querySelectorAll('input[name="lectureIds"]').forEach(cb =>
    cb.addEventListener('change', updateCartSummary)
  );
  document.getElementById('checkoutBtn').addEventListener('click', validateCheckout);

  updateCartSummary(); // 초기 렌더링
</script>

  


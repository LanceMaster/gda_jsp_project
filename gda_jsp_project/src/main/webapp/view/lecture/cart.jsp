<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

  <title>장바구니</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/cart.css" />
  <h2>🛒 신청하기 - Step1 강의 선택</h2>

  <form method="post" action="${pageContext.request.contextPath}/checkout" class="cart-container">
    <!-- 좌측: 강의 리스트 -->
    <div class="lecture-list">
      <c:forEach var="lec" items="${cartLectures}">
        <div class="lecture-item">
          <label>
            <input type="checkbox" name="lectureIds" value="${lec.lectureId}" data-price="${lec.price}" checked />
            <img src="${lec.thumbnail}" alt="${lec.title}" />
            <div class="lecture-info">
              <div class="lecture-title">${lec.title}</div>
              <div class="lecture-price">
                <fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" />
              </div>
            </div>
          </label>
        </div>
      </c:forEach>
    </div>

    <!-- 우측: 요약 정보 -->
    <div class="summary-box">
      <h3>주문 요약</h3>
      <ul class="summary-list">
        <c:forEach var="lec" items="${cartLectures}">
          <li class="summary-item">
            ${lec.title} 
            <span>
              <fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" />
            </span>
          </li>
        </c:forEach>
      </ul>
      <div class="total-section">
        Total: ₩<span id="totalPrice">0</span>
      </div>
      <button type="submit" id="checkoutBtn">결제하기</button>
    </div>
  </form>

  <script>
    function updateTotal() {
      let total = 0;
      document.querySelectorAll('input[name="lectureIds"]:checked').forEach(cb => {
        const price = parseInt(cb.dataset.price, 10);
        total += price;
      });
      document.getElementById('totalPrice').textContent = total.toLocaleString();
    }

    document.querySelectorAll('input[name="lectureIds"]').forEach(cb =>
      cb.addEventListener('change', updateTotal)
    );

    updateTotal();
  </script>

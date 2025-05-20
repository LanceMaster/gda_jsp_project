<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>장바구니</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/cart.css" />

<h2>🛒 신청하기 - Step 1. 강의 선택</h2>

<form method="post"
	action="${pageContext.request.contextPath}/cart/checkout"
	class="cart-container">

	<div class="lecture-list">
		<c:choose>
			<c:when test="${not empty cartLectures}">
				<c:forEach var="lec" items="${cartLectures}">
					<div class="lecture-item" data-id="${lec.lectureId}">
						<label> <input type="checkbox" name="lectureIds"
							value="${lec.lectureId}" data-price="${lec.price}"
							data-title="${lec.title}" checked /> <img
							src="<c:url value='${lec.thumbnail}' />" alt="${lec.title}"
							class="lecture-thumbnail" />
							<div class="lecture-info">
								<div class="lecture-title">${lec.title}</div>
								<div class="lecture-price">
									<fmt:formatNumber value="${lec.price}" type="currency"
										currencySymbol="₩" />
								</div>
							</div>
						</label>
						<button type="button" class="delete-btn"
							data-id="${lec.lectureId}">❌</button>
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

	<div class="summary-box">
		<h3>주문 요약</h3>
		<ul class="summary-list" id="summaryList"></ul>
		<div class="total-section">
			Total: ₩<span id="totalPrice">0</span>
		</div>
		<div id="priceInputs"></div>
		<button type="submit" id="checkoutBtn" class="checkout-btn">결제하기</button>
	</div>
</form>

<script>
    function updateCartSummary() {
        var total = 0;
        var summaryList = document.getElementById('summaryList');
        var priceInputsContainer = document.getElementById('priceInputs');
        summaryList.innerHTML = '';
        priceInputsContainer.innerHTML = '';

        var checkedItems = document.querySelectorAll('input[name="lectureIds"]:checked');
        for (var i = 0; i < checkedItems.length; i++) {
            var cb = checkedItems[i];
            var lectureId = cb.value;
            var price = parseInt(cb.dataset.price, 10);
            var title = cb.dataset.title;

            if (!isNaN(price)) {
                total += price;

                var li = document.createElement('li');
                li.className = 'summary-item';
                li.innerHTML = "[" + lectureId + "] " + title + " - ₩" + price.toLocaleString();
                summaryList.appendChild(li);

                var hidden = document.createElement('input');
                hidden.type = 'hidden';
                hidden.name = 'amounts';
                hidden.value = price;
                priceInputsContainer.appendChild(hidden);
            }
        }

        document.getElementById('totalPrice').textContent = total.toLocaleString();
    }

    function validateCheckout(e) {
        var checked = document.querySelectorAll('input[name="lectureIds"]:checked');
        if (checked.length === 0) {
            alert("📛 최소 하나의 강의를 선택해주세요.");
            e.preventDefault();
        }
    }

    function bindDeleteButtons() {
        var deleteButtons = document.querySelectorAll('.delete-btn');
        for (var i = 0; i < deleteButtons.length; i++) {
            deleteButtons[i].addEventListener('click', function () {
                var lectureId = this.getAttribute('data-id');
                if (confirm("이 강의를 장바구니에서 삭제하시겠습니까?")) {
                    fetch("<%= request.getContextPath() %>/cart/remove?lectureId=" + lectureId, {
                        method: 'GET'
                    }).then(function (res) {
                        if (res.ok) {
                            location.reload();
                        } else {
                            alert("삭제 실패");
                        }
                    });
                }
            });
        }
    }

    document.getElementById('checkoutBtn').addEventListener('click', validateCheckout);
    updateCartSummary();
    bindDeleteButtons();

    var checkboxes = document.querySelectorAll('input[name="lectureIds"]');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].addEventListener('change', updateCartSummary);
    }
</script>

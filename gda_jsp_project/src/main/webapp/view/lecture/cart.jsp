<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>장바구니</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/cart.css" />

<h2>🛒 신청하기 - Step 1. 강의 선택</h2>

<div class="cart-container">

    <div class="lecture-list">
        <c:choose>
            <c:when test="${not empty cartLectures}">
                <c:forEach var="lec" items="${cartLectures}">
                    <div class="lecture-item" data-id="${lec.lectureId}">
                        <label> 
                            <input type="checkbox" name="lectureIds"
                                   value="${lec.lectureId}" data-price="${lec.price}"
                                   data-title="${lec.title}" checked />
                            <img src="<c:url value='${lec.thumbnail}' />" alt="${lec.title}" class="lecture-thumbnail" />
                            <div class="lecture-info">
                                <div class="lecture-title">${lec.title}</div>
                                <div class="lecture-price">
                                    <fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" />
                                </div>
                            </div>
                        </label>
                        <button type="button" class="delete-btn" data-id="${lec.lectureId}">❌</button>
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
        <button id="checkoutBtn" class="checkout-btn">결제하기</button>
    </div>
</div>

<script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<script>
    function updateCartSummary() {
        var total = 0;
        var summaryList = document.getElementById('summaryList');
        summaryList.innerHTML = '';

        var checkedItems = document.querySelectorAll('input[name="lectureIds"]:checked');
        checkedItems.forEach(function(cb) {
            var lectureId = cb.value;
            var price = parseInt(cb.dataset.price, 10);
            var title = cb.dataset.title;

            if (!isNaN(price)) {
                total += price;

                var li = document.createElement('li');
                li.className = 'summary-item';
                li.textContent = "[" + lectureId + "] " + title + " - ₩" + price.toLocaleString();
                summaryList.appendChild(li);
            }
        });

        document.getElementById('totalPrice').textContent = total.toLocaleString();
    }

    function bindDeleteButtons() {
        var deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                var lectureId = this.getAttribute('data-id');
                if (confirm("이 강의를 장바구니에서 삭제하시겠습니까?")) {
                    fetch("${pageContext.request.contextPath}/cart/remove?lectureId=" + lectureId, {
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
        });
    }

    // 아임포트 결제 요청 함수
    function requestPayment(lectureIds, amounts, titles) {
    	
    	console.log("requestPayment 호출 - 강의ID들:", lectureIds);
        console.log("requestPayment 호출 - 가격들:", amounts);
        console.log("requestPayment 호출 - 제목들:", titles);
        
        var IMP = window.IMP;
        IMP.init('imp86227668'); // 아임포트 가맹점 코드 입력

        var totalAmount = amounts.reduce((a, b) => a + b, 0);

        IMP.request_pay({
        	  pg: 'kakaopay',
              pay_method: 'card',
              merchant_uid: 'merchant_' + new Date().getTime(),
              name: '테스트 결제',
              amount: totalAmount,
              buyer_email: 'test@example.com',
              buyer_name: '홍길동',
              buyer_tel: '010-1234-5678'
        }, function (rsp) {
            if (rsp.success) {
                sendCheckoutRequest(rsp.imp_uid, lectureIds, amounts);
            } else {
                alert("결제 실패: " + rsp.error_msg);
            }
        });
    }

    // 서버에 JSON으로 결제 정보 전송
    function sendCheckoutRequest(impUid, lectureIds, amounts) {
        fetch("${pageContext.request.contextPath}/cart/checkout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                imp_uid: impUid,
                lectureIds: lectureIds,
                amounts: amounts
            })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert("✅ 결제 및 수강신청이 완료되었습니다.");
                location.href = "${pageContext.request.contextPath}/user/mypage";
            } else {
                alert("❌ " + data.message);
            }
        })
        .catch(err => {
            console.error("서버 에러:", err);
            alert("🚨 서버 오류 발생");
        });
    }

    document.getElementById('checkoutBtn').addEventListener('click', function (e) {
        e.preventDefault();

        var checkedItems = document.querySelectorAll('input[name="lectureIds"]:checked');
        if (checkedItems.length === 0) {
            alert("📛 최소 하나의 강의를 선택해주세요.");
            return;
        }

        var lectureIds = [];
        var amounts = [];
        var titles = [];

        checkedItems.forEach(function(cb) {
            lectureIds.push(parseInt(cb.value));
            amounts.push(parseInt(cb.dataset.price));
            titles.push(cb.dataset.title);
        });

        // 실제 결제 요청 함수 호출
        requestPayment(lectureIds, amounts, titles);
    });

    updateCartSummary();
    bindDeleteButtons();

    var checkboxes = document.querySelectorAll('input[name="lectureIds"]');
    checkboxes.forEach(cb => cb.addEventListener('change', updateCartSummary));
</script>

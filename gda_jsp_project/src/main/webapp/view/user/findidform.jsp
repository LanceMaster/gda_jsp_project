<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<title>아이디 찾기</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/userlogin.css" />
</head>

<body>
	<div class="login-modal-custom position-relative">
		<div class="popup-close-btn">&times;</div>
		<div class="login-title">아이디 찾기</div>
		<div class="login-desc">가입 시 등록한 이메일을 입력하세요.</div>

		<form id="findIdForm"
			action="${pageContext.request.contextPath}/AccountServlet"
			method="post" autocomplete="off">
			<input type="hidden" name="action" value="findIdSubmit" />

			<div class="form-group email-group">
				<div class="input-wrap" style="flex: 1">
					<span class="input-icon"><i class="fas fa-envelope"></i></span> <input
						type="email" name="email" id="emailInput" class="form-control"
						placeholder="등록된 이메일 주소를 입력하세요" required />
				</div>
			</div>

			<button type="submit" class="btn login-btn-main"
				style="background: #6c6ce5; width: 100%; margin-top: 10px;">
				아이디 찾기</button>

		<%--로그인 버튼 --%>
            <div class="bottom-link">
                <a href="#" class="go-login">로그인</a>
            </div>
		</form>
	</div>
</body>

<script>
$(document)
.off("click", ".go-login")
.on(
	"click",
	".go-login",
	function (e) {
		e.preventDefault();
		$
			.ajax({
				url: "${pageContext.request.contextPath}/user/loginform",
				method: "POST",
				success: function (data) {
					$(".popup-content").html(data);
					$("#loginPopupOverlay")
						.removeClass("d-none");
				},
				error: function () {
					alert("로그인 폼을 불러오지 못했습니다.");
				},
			});
	});

</script>
</html>

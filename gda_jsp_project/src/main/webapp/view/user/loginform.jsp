<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>
<style>
#loginPopupOverlay, .modal-overlay {
	position: fixed;
	left: 0;
	top: 0;
	width: 100vw;
	height: 100vh;
	background: rgba(0, 0, 0, 0.18);
	z-index: 9998;
	display: flex;
	align-items: center;
	justify-content: center;
}

.login-modal-custom {
	position: relative;
	z-index: 9999;
	max-width: 800px; /* 크기 크게 */
	margin: 0 auto;
	background: #fff;
	border-radius: 12px;
	padding: 32px 32px 24px 32px;
	font-family: 'Noto Sans KR', sans-serif;
}

.login-modal-custom .login-title {
	font-size: 2rem;
	font-weight: 700;
	text-align: center;
	margin-bottom: 6px;
}

.login-modal-custom .login-desc {
	text-align: center;
	color: #888;
	margin-bottom: 16px;
	font-size: 1.1rem;
}

.login-modal-custom .form-control {
	background: #f5f6fa;
	border: none;
	border-radius: 8px;
	margin-bottom: 10px;
	font-size: 1.1rem;
	padding-left: 36px;
	height: 44px;
}

.login-modal-custom .input-icon {
	position: absolute;
	left: 12px;
	top: 50%;
	transform: translateY(-50%);
	color: #bbb;
	font-size: 1.15rem;
}

.login-modal-custom .form-group {
	position: relative;
}

.login-modal-custom .login-btn-main {
	width: 100%;
	background: #6C6CE5;
	color: #fff;
	font-weight: 600;
	border: none;
	border-radius: 8px;
	margin: 14px 0 16px 0;
	padding: 12px 0;
	font-size: 1.1rem;
	transition: background 0.2s;
}

.login-modal-custom .login-btn-main:hover {
	background: #5757c9;
}

.login-modal-custom .login-links {
	display: flex;
	justify-content: space-between;
	font-size: 1rem;
	margin-bottom: 10px;
}

.login-modal-custom .or-divider {
	display: flex;
	align-items: center;
	text-align: center;
	margin: 18px 0 12px 0;
	font-size: 1rem;
}

.login-modal-custom .or-divider::before, .login-modal-custom .or-divider::after
	{
	content: '';
	flex: 1;
	border-bottom: 1px solid #eee;
}

.login-modal-custom .or-divider:not(:empty)::before {
	margin-right: .7em;
}

.login-modal-custom .or-divider:not(:empty)::after {
	margin-left: .7em;
}

.login-modal-custom .sns-btn {
	width: 100%;
	border-radius: 8px;
	border: none;
	margin-bottom: 10px;
	font-size: 1.05rem;
	font-weight: 500;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 8px;
	padding: 11px 0;
}

.sns-naver {
	background: #e8f5e9;
	color: #1ec800;
}

.sns-kakao {
	background: #fff7e0;
	color: #3c1e1e;
}

.sns-google {
	background: #ffeaea;
	color: #ea4335;
}

.login-modal-custom .bottom-link {
	text-align: center;
	margin-top: 10px;
	font-size: 1rem;
}

.login-modal-custom .bottom-link a {
	color: #6C6CE5;
	text-decoration: none;
	font-weight: 500;
}

.popup-close-btn {
	position: absolute;
	top: 12px;
	right: 16px;
	font-size: 1.3rem;
	color: #bbb;
	cursor: pointer;
	z-index: 10000;
}
</style>
</head>
<div class="login-modal-custom position-relative">
	<div class="popup-close-btn">&times;</div>
	<div class="login-title">로그인</div>
	<div class="login-desc">당신의 계정에 접속하세요</div>
	<c:if test="${not empty loginError}">
		<div class="alert alert-danger login-error">${loginError}</div>
	</c:if>
	<form id="loginForm"
		action="${pageContext.request.contextPath}/user/login" method="post"
		autocomplete="off">
		<div class="form-group">
			<span class="input-icon"><i class="fas fa-user"></i></span> <input
				type="text" name="id" class="form-control" placeholder="아이디를 입력하세요"
				required />
		</div>
		<div class="form-group">
			<span class="input-icon"><i class="fas fa-lock"></i></span> <input
				type="password" name="password" class="form-control"
				placeholder="비밀번호를 입력하세요" required />
		</div>
		<div class="login-links">
			<a href="#">아이디를 잊으셨나요?</a> <a href="#">비밀번호를 잊으셨나요?</a>
		</div>
		<button type="submit" class="btn login-btn-main">로그인</button>
		<div class="or-divider">Or</div>
		<button type="button" class="sns-btn sns-naver">
			<i class="fab fa-google"></i>Continue with Naver
		</button>
		<button type="button" class="sns-btn sns-kakao">
			<i class="fab fa-google"></i>Continue with Kakao
		</button>
		<button type="button" class="sns-btn sns-google">
			<i class="fab fa-google"></i>Continue with Google
		</button>
		<div class="bottom-link">
			계정이 필요하신가요? <a href="#">회원가입</a>
		</div>
	</form>
</div>
<script>
  $(document).on("submit", "#loginForm", function(e) {
    e.preventDefault();
    $.ajax({
        url: $(this).attr("action"),
        method: "POST",
        data: $(this).serialize(),
        dataType: "html",
        success: function(data) {
            // 실패 시 loginform.jsp(에러 메시지 포함)가 반환됨
            if (data.includes("login-error") || data.includes("alert-danger")) {
                $(".popup-content").html(data);
            } else {
                // 성공 시 강제로 새로고침(세션이 적용된 메인페이지로)
                location.reload();
            }
        },
        error: function() {
            alert("로그인 중 오류가 발생했습니다.");
        }
    });
});
  //회원가입 버튼 클릭 시
  $(document).on("click", ".bottom-link a", function(e) {
    e.preventDefault();
    $.ajax({
        url: "${pageContext.request.contextPath}/user/signupform",
        method: "GET",
        success: function(data) {
            $(".popup-content").html(data);
        },
        error: function() {
            alert("회원가입 폼을 불러오지 못했습니다.");
        }
    });
    });
</script>
</html>

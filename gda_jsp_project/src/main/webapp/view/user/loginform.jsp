<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>

<!-- Font Awesome CDN -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

<!-- Bootstrap CSS CDN -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />

<!-- jQuery UI CSS CDN -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css" />

<!-- 사용자 CSS (로컬 경로 유지) -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/userlogin.css" />

</head>
<body>
<div class="login-modal-custom position-relative">
    <!-- 홈으로 가기 버튼 -->
    <a href="${pageContext.request.contextPath}/" class="home-btn position-absolute" style="top: 10px; left: 10px; font-size: 18px; text-decoration: none;">
        <i class="fas fa-home"></i> 홈
    </a>
    <div class="login-title">로그인</div>
    <div class="login-desc">당신의 계정에 접속하세요</div>
    <c:if test="${not empty loginError}">
        <div class="alert alert-danger login-error">${loginError}</div>
    </c:if>
    <form id="loginForm"
        action="${pageContext.request.contextPath}/user/login" method="post"
        autocomplete="off">
        <div class="form-group">
            <span class="input-icon"><i class="fas fa-user"></i></span> 
            <input type="text" name="id" class="form-control" placeholder="아이디를 입력하세요" required />
        </div>
        <div class="form-group">
            <span class="input-icon"><i class="fas fa-lock"></i></span> 
            <input type="password" name="password" class="form-control" placeholder="비밀번호를 입력하세요" required />
        </div>
        <div class="login-links">
            <a href="#">아이디를 잊으셨나요?</a> 
            <a href="#">비밀번호를 잊으셨나요?</a>
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
            계정이 필요하신가요? <a href="${pageContext.request.contextPath}/user/signupform">회원가입</a>
        </div>
    </form>
    
<!-- jQuery CDN (먼저 로드해야 함!) -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- jQuery UI CDN -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

<!-- Bootstrap JS CDN -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</div>
</body>
</html>

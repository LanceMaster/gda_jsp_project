<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/fontawesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
<script
	src="${pageContext.request.contextPath}/static/js/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/userlogin.css" />

<script
	src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.min.js"></script>
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
			계정이 필요하신가요? <a
				href="${pageContext.request.contextPath}/user/signupform">회원가입</a>
		</div>
	</form>
</div>


</html>
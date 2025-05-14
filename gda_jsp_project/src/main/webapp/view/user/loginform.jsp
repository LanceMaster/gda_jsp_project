<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/userlogin.css">
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
			계정이 필요하신가요? <a href="javascript:fun_btn()" >회원가입</a>
		</div>
	</form>
</div>
<script>
	//중복바인딩 제거
  $(document).off("submit", "#loginForm").on("submit", "#loginForm", function(e) {
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
//  $(document).on("click", ".bottom-link a", function(e) {
//   	window.alert("회원가입 버튼 클릭");
//    });
//   function fun_btn() {
// 	  window.alert("회원가입 버튼 클릭");
// }

// 회원가입 버튼 클릭 시
$(document).off("click", ".bottom-link a").on("click", ".bottom-link a", function(e) {
	//링크는이동안하고 loginform에서 signupform으로 이동
	e.preventDefault();
	//회원가입 페이지로 이동
	$.ajax({
        url: "${pageContext.request.contextPath}/user/signupform",
        method: "POST",
        success: function(data) {
            $(".popup-content").html(data);
            $("#loginPopupOverlay").removeClass("d-none");
        },
        error: function() {
            alert("회원가입 폼을 불러오지 못했습니다.");
        }
    });
	
});

  
</script>
</html>

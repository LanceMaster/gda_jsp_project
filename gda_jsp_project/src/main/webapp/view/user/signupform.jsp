<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>회원가입</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/userlogin.css" />
</head>
<body>
	<div class="login-modal-custom position-relative">
		<div class="popup-close-btn">&times;</div>
		<div class="login-title">수강생 회원가입</div>
		<div class="login-desc">회원 가입을 위해 정보를 입력하세요.</div>
		<div class="tab-bar"
			style="display: flex; gap: 24px; justify-content: center; margin-bottom: 18px;">
			<button type="button" class="tab-btn active" data-role="student"
				style="border: none; background: none; font-size: 1.1rem; font-weight: 600; color: #6c6ce5; border-bottom: 2.5px solid #6c6ce5; padding: 6px 18px 8px 18px; border-radius: 0;">
				수강생</button>
			<button type="button" class="tab-btn" data-role="teacher"
				style="border: none; background: none; font-size: 1.1rem; font-weight: 600; color: #888; border-bottom: 2.5px solid transparent; padding: 6px 18px 8px 18px; border-radius: 0;">
				강사</button>
		</div>
		<div class="email-guide">이메일이 아이디로 사용됩니다.</div>
		<form id="signupForm"
			action="${pageContext.request.contextPath}/user/signup" method="post"
			autocomplete="off" enctype="multipart/form-data">
			<input type="hidden" name="role" id="roleInput" value="student" />
			<div class="form-group email-group">
				<div class="input-wrap" style="flex: 2">
					<span class="input-icon"><i class="fas fa-envelope"></i></span> <input
						type="email" name="email" id="emailInput" class="form-control"
						placeholder="이메일 주소를 입력하세요" required />
				</div>
				<button type="button" class="btn login-btn-main"
					id="emailDupCheckBtn"
					style="flex: 1; min-width: 100px; margin-left: 8px">
					중복확인</button>
				<button type="button" class="btn login-btn-main" id="emailVerifyBtn"
					style="flex: 1; min-width: 120px; margin-left: 8px" disabled>
					이메일 인증하기</button>
			</div>
			<!-- 인증코드 입력란 (초기에는 hidden) -->
			<div class="form-group" id="authCodeGroup" style="display: none">
				<input type="text" id="authCodeInput" class="form-control"
					placeholder="인증코드를 입력하세요" style="flex: 2" />
				<button type="button" class="btn login-btn-main"
					id="authCodeCheckBtn"
					style="flex: 1; min-width: 100px; margin-left: 8px">
					인증 확인</button>
			</div>
			<div class="form-group">
				<span class="input-icon"><i class="fas fa-user"></i></span> <input
					type="text" name="name" class="form-control"
					placeholder="이름을 입력하세요" required />
			</div>
			<div class="form-group">
				<span class="input-icon"><i class="fas fa-lock"></i></span> <input
					type="password" name="password" class="form-control"
					placeholder="비밀번호를 입력하세요" required />
			</div>
			<div class="form-group">
				<span class="input-icon"><i class="fas fa-lock"></i></span> <input
					type="password" name="passwordCheck" class="form-control"
					placeholder="비밀번호를 다시 입력하세요" required />
			</div>
			<div class="form-group">
				<span class="input-icon"><i class="fas fa-mobile-alt"></i></span> <input
					type="text" name="phone" class="form-control"
					placeholder="휴대폰 번호를 입력하세요" required />
			</div>
			<div class="form-group">
				<span class="input-icon"><i class="fas fa-calendar-alt"></i></span>
				<input type="date" name="birthdate" class="form-control"
					placeholder="생년월일을 입력하세요" required />
			</div>
			<div class="gender-group">
				<label style="margin-bottom: 0"> <input type="radio"
					name="gender" value="M" required /> 남
				</label> <label style="margin-bottom: 0"> <input type="radio"
					name="gender" value="F" required /> 여
				</label>
			</div>

			<div class="form-group" id="resumeGroup" style="display: none">
				<span class="input-icon"><i class="fas fa-file-upload"></i></span> <input
					type="file" name="resume" id="resumeInput" class="form-control"
					style="padding-left: 36px; background: #f5f6fa"
					accept=".pdf,.hwp,.doc,.docx" /> <span
					style="font-size: 0.98rem; color: #888; margin-left: 8px">이력서
					파일 (PDF, HWP, DOC, DOCX)</span>
			</div>

			<div class="agree-group">
				<label style="font-size: 0.98rem"> <input type="checkbox"
					name="agree" required /> 이용 약관에 동의합니다.
				</label>
			</div>
			<button type="submit" class="btn login-btn-main"
				style="background: #6c6ce5">제출</button>
			<div class="bottom-link">
				이미 회원이신가요? <a href="#" class="go-login">로그인</a>
			</div>
		</form>
	</div>

	<!-- <script>
  document.getElementById("emailDupCheckBtn").onclick = function () {
    var email = document.getElementById("emailInput").value;
    if (!email) {
      alert("이메일을 입력하세요.");
      return;
    }
    // 컨트롤러로 GET 방식 이동 (새 창으로 열기)
    window.location.href =
      "${pageContext.request.contextPath}/user/emailDupCheck?email=" + encodeURIComponent(email);
  };
</script> -->



<script>
 $("#emailDupCheckBtn").on("click", function () {
  var email = $("#emailInput").val();
  if (!email) {
    alert("이메일을 입력하세요.");
    return;
  }
  $.ajax({
    url: "${pageContext.request.contextPath}/AccountServlet",
    type: "POST",
    data: {
      action: "emailDupCheck",
      email: email
    },
    success: function (result) {
      result = $.trim(result);
      if (result === "available") {
        alert("사용 가능한 이메일입니다.");
        $("#emailInput").prop("readonly", true);
        $("#emailVerifyBtn").prop("disabled", false).css({
          background: "#6c6ce5",
          cursor: "pointer"
        });
      } else {
        alert("이미 사용 중인 이메일입니다.");
      }
    },
    error: function (e) {
      alert("중복확인 요청 실패: " + e.status);
    }
  });
});
</script>











	<script>
      let serverAuthCode = ""; // 실제로는 서버에서 발송된 인증코드와 비교해야 함










      $(document)
        .off("click", ".tab-btn")
        .on("click", ".tab-btn", function () {
          $(".tab-btn").removeClass("active").css({
            color: "#888",
            borderBottom: "2.5px solid transparent",
          });
          $(this).addClass("active").css({
            color: "#6c6ce5",
            borderBottom: "2.5px solid #6c6ce5",
          });
          const role = $(this).data("role");
          $("#roleInput").val(role);
          if (role === "teacher") {
            $("#resumeGroup").show();
            $("#resumeInput").attr("required", true);
          } else {
            $("#resumeGroup").hide();
            $("#resumeInput").removeAttr("required");
          }
        });
    </script>

	<script>
      $(document)
        .off("click", ".go-login")
        .on("click", ".go-login", function (e) {
          e.preventDefault();
          $.ajax({
            url: "${pageContext.request.contextPath}/user/loginform",
            method: "POST",
            success: function (data) {
              $(".popup-content").html(data);
              $("#loginPopupOverlay").removeClass("d-none");
            },
            error: function () {
              alert("로그인 폼을 불러오지 못했습니다.");
            },
          });
        });
    </script>


</body>
</html>
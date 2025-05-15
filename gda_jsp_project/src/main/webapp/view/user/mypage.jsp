<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<style>
body {
	background: #fff;
}

.mypage-wrap {
	display: flex;
	max-width: 1200px;
	margin: 40px auto;
}

.mypage-left, .mypage-right {
	flex: 1;
}

.mypage-left {
	margin-right: 40px;
}

.profile-card, .lecture-card {
	background: #fff;
	border-radius: 16px;
	box-shadow: 0 2px 8px #eee;
	padding: 20px;
	margin-bottom: 24px;
}

.lecture-img {
	width: 80px;
	height: 60px;
	object-fit: cover;
	border-radius: 8px;
	margin-right: 20px;
}

.lecture-info {
	flex: 1;
}

.progress {
	height: 8px;
	margin-bottom: 8px;
}

.profile-form input, .profile-form select {
	margin-bottom: 16px;
}

.profile-form label {
	font-weight: 500;
}
</style>
</head>

<body>

	<div class="mypage-wrap">
		<!-- 왼쪽 영역: 강의 -->
		<div class="mypage-left">
			<div class="profile-card">
				<h4>${user.name}</h4>
				<div style="color: #888;">직급: 회원</div>
				<a href="#"
					style="font-size: 0.95rem; color: #6c6ce5; text-decoration: underline;">수강강의
					전체</a>
			</div>

			<!-- 예시 강의 카드 -->
			<div class="lecture-card d-flex align-items-center">
				<img
					src="https://images.unsplash.com/photo-1519389950473-47ba0277781c"
					class="lecture-img" alt="강의 이미지" />
				<div class="lecture-info">
					<div style="font-weight: 600;">CSS</div>
					<div style="font-size: 0.95rem; color: #888;">전체수강일: 50회</div>
					<div class="progress">
						<div class="progress-bar" style="width: 30%; background: #6c6ce5;"></div>
					</div>
					<div style="font-size: 0.95rem;">진도율: 30%</div>
				</div>
				<button class="btn btn-primary"
					style="background: #6c6ce5; border: none;">수강</button>
			</div>
		</div>

		<!-- 오른쪽 영역: 프로필 -->
		<div class="mypage-right">
			<h3 style="font-weight: 700; margin-bottom: 32px;">프로필</h3>
			<form class="profile-form">
				<div class="form-group">
					<label><i class="fas fa-user"></i> 이름</label> <input type="text"
						class="form-control" value="${user.name}" readonly />
				</div>
				<div class="form-group">
					<label><i class="fas fa-envelope"></i> 이메일</label> <input
						type="email" class="form-control" value="${user.email}" readonly />
				</div>
				<div class="form-group">
					<label><i class="fas fa-lock"></i> 비밀번호</label> <input
						type="password" class="form-control" value="********" readonly />
				</div>
				<div class="form-group">
					<label><i class="fas fa-phone"></i> 연락처</label> <input type="text"
						class="form-control" value="${user.phone}" readonly />
				</div>
				<fmt:formatDate value="${user.birthdate}" pattern="yyyy-MM-dd"
					var="birthDateFormatted" />
				<div class="form-group">
					<label><i class="fas fa-calendar"></i> 생년월일</label> <input
						type="date" class="form-control" value="${birthDateFormatted}"
						readonly />
				</div>

				<button type="button" class="btn btn-primary btn-block"
					style="background: #6c6ce5;" data-toggle="modal"
					data-target="#emailModal">수정</button>
			</form>
			<div class="text-right mt-2">
				<a href="#" style="color: #6c6ce5; font-size: 0.95rem;">탈퇴하기</a>
			</div>
		</div>
	</div>

	<!-- 이메일 인증/비밀번호 변경 모달 -->
	<div class="modal fade" id="emailModal" tabindex="-1" role="dialog"
		aria-labelledby="emailModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content p-3">
				<div class="modal-header">
					<h5 class="modal-title">이메일 인증</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="닫기">
						<span>&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<%--         <input type="email" id="emailInput" class="form-control mb-2" value="${user.email}" readonly />
 --%>
					<input type="email" id="emailInput" class="form-control mb-2"
						value="ukirrer@naver.com" readonly />
					<button class="btn btn-secondary btn-sm mb-2"
						onclick="sendVerification()">인증번호 전송</button>
					<input type="text" id="codeInput" class="form-control mb-2"
						placeholder="인증번호 입력" />
					<button class="btn btn-success btn-sm" onclick="verifyCode()">인증
						확인</button>
					<div id="verifyResult" class="mt-2 text-danger"></div>

					<div class="form-group mt-3" id="passwordFields"
						style="display: none;">
						<input type="password" id="newPassword" class="form-control mb-2"
							placeholder="새 비밀번호" /> <input type="password"
							id="confirmPassword" class="form-control mb-2"
							placeholder="새 비밀번호 확인" />
						<div id="passwordMatchMessage"
							style="margin-top: 6px; font-weight: 500;"></div>

						<button class="btn btn-primary btn-sm" id="changePasswordBtn"
							onclick="changePassword()" disabled>비밀번호 변경</button>
					</div>
				</div>
			</div>
		</div>
	</div>



	<script>
  function sendVerification() {
    const email = $("#emailInput").val();
    $.post("${pageContext.request.contextPath}/AccountServlet", { action: "sendAuthCode", email }, function(response) {
      alert(response.trim() === "sent" ? "인증번호가 이메일로 전송되었습니다." : "인증번호 전송 실패");
    });
  }

  function verifyCode() {
    const code = $("#codeInput").val();
    $.post("${pageContext.request.contextPath}/AccountServlet", { action: "checkAuthCode", authCode: code }, function(response) {
      if (response.trim() === "success") {
        $("#verifyResult").text("✅ 인증 성공!").removeClass("text-danger").addClass("text-success");
        $("#passwordFields").slideDown();
        
     // 인증 성공 시 버튼과 입력란 비활성화
        $("#emailInput").prop("disabled", true);
        $("#codeInput").prop("disabled", true);
        $(".btn-secondary").prop("disabled", true);  // 인증번호 전송 버튼
        $(".btn-success").prop("disabled", true);    // 인증 확인 버튼
      } else {
        $("#verifyResult").text("❌ 인증 실패. 다시 시도하세요.").removeClass("text-success").addClass("text-danger");
      }
    });
  }

  function changePassword() {
    const newPw = $("#newPassword").val();
    const email = $("#emailInput").val();

    $.post("${pageContext.request.contextPath}/AccountServlet", {
      action: "changePassword",
      email: email,
      newPassword: newPw
    }, function(response) {
      if (response.trim() === "success") {
        alert("비밀번호가 변경되었습니다. 다시 로그인해 주세요.");
        window.location.href = "${pageContext.request.contextPath}/user/logout"; // 로그아웃 후 메인 이동
      } else {
        alert("비밀번호 변경 실패");
      }
    });

    return false; // 버튼 기본 제출 방지
  }

  $(document).on('input', '#newPassword, #confirmPassword', function () {
    const pw1 = $('#newPassword').val();
    const pw2 = $('#confirmPassword').val();
    const $msg = $('#passwordMatchMessage');
    

    if (!pw1 && !pw2) {
      $msg.text(''); // 둘 다 비어있으면 메시지 삭제
      $('#changePasswordBtn').prop('disabled', true);
      return;
    }

    if (pw1 === pw2) {
        $msg.text('✅ 비밀번호가 일치합니다.').css('color', 'green');
        $('#changePasswordBtn').prop('disabled', !pw1); // pw1이 비어있으면 disabled
      } else {
        $msg.text('❌ 비밀번호가 일치하지 않습니다.').css('color', 'red');
        $('#changePasswordBtn').prop('disabled', true);
      }
   
  });
</script>
</body>
</html>

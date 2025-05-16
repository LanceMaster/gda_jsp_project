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
		<!-- 왼쪽 강의 카드 -->
		<div class="mypage-left">
			<div class="profile-card">
				<h4>${user.name}</h4>
				<div style="color: #888;">
					직급:
					<c:choose>
						<c:when test="${user.role == 'STUDENT'}">수강생</c:when>
						<c:when test="${user.role == 'INSTRUCTOR'}">강사</c:when>
						<c:otherwise>회원</c:otherwise>
					</c:choose>
				</div>
				<a href="#"
					style="font-size: 0.95rem; color: #6c6ce5; text-decoration: underline;">수강강의
					전체</a>
			</div>
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

		<!-- 오른쪽 프로필 -->
		<div class="mypage-right">

			<c:if test="${user.role == 'INSTRUCTOR'}">
				<div style="margin-bottom: 16px;">
					<ul
						style="display: flex; list-style: none; padding: 0; margin: 0; border-bottom: 2px solid #6c6ce5;">
						<li style="margin-right: 24px;"><a
							href="${pageContext.request.contextPath}/lecture/lecturelist"
							style="text-decoration: none; padding: 8px 0; display: inline-block; color: #6c6ce5; font-weight: bold; border-bottom: 3px solid #6c6ce5;">
								나의 강의 </a></li>
					</ul>
				</div>
			</c:if>



			<h3 style="font-weight: 700; margin-bottom: 32px;">프로필</h3>
			<form class="profile-form">
				<div class="form-group">
					<label>이름</label> <input type="text" class="form-control"
						value="${user.name}" readonly />
				</div>
				<div class="form-group">
					<label>이메일</label> <input type="email" class="form-control"
						value="${user.email}" readonly />
				</div>
				<div class="form-group">
					<label>비밀번호</label> <input type="password" class="form-control"
						value="********" readonly />
				</div>
				<div class="form-group">
					<label>연락처</label> <input type="text" class="form-control"
						value="${user.phone}" readonly />
				</div>
				<fmt:formatDate value="${user.birthdate}" pattern="yyyy-MM-dd"
					var="birthDateFormatted" />
				<div class="form-group">
					<label>생년월일</label> <input type="date" class="form-control"
						value="${birthDateFormatted}" readonly />
				</div>
				<button type="button" class="btn btn-primary btn-block"
					style="background: #6c6ce5;" data-toggle="modal"
					data-target="#emailModal">수정</button>
			</form>

			<div class="text-right mt-2">
				<a href="#" data-toggle="modal" data-target="#deleteModal"
					style="color: #6c6ce5; font-size: 0.95rem;">탈퇴하기</a>
			</div>
		</div>
	</div>

	<!-- 이메일 인증 모달 -->
	<div class="modal fade" id="emailModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content p-3">
				<div class="modal-header">
					<h5 class="modal-title">이메일 인증</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<input type="email" id="emailInput" class="form-control mb-2"
						value="${user.email}" readonly />
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
							onclick="changePassword(event)" disabled>비밀번호 변경</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 회원탈퇴 모달 (생략 가능) -->

	<!-- ✅ 스크립트 (jQuery 포함 후 실행) -->
	<script>
	 const user = {
			    id: "${user.userId}",
			    name: "${user.name}",
			    email: "${user.email}",
			    phone: "${user.phone}",
			    birthdate: "${birthDateFormatted}",
			    role : "${user.role}"
			  };
			  console.log("로그인된 사용자 정보:", user);
	
function sendVerification() {
  const email = $("#emailInput").val();
  $.post("${pageContext.request.contextPath}/AccountServlet", { action: "sendVerificationEmail", email }, function(response) {
    alert(response.trim() === "sent" ? "인증번호가 이메일로 전송되었습니다." : "인증번호 전송 실패");
  });
}

function verifyCode() {
  const code = $("#codeInput").val();
  $.post("${pageContext.request.contextPath}/AccountServlet", { action: "checkAuthCode", authCode: code }, function(response) {
    if (response.trim() === "success") {
      $("#verifyResult").text("✅ 인증 성공!").removeClass("text-danger").addClass("text-success");
      $("#passwordFields").slideDown();
      $("#emailInput, #codeInput").prop("disabled", true);
      $(".btn-secondary, .btn-success").prop("disabled", true);
    } else {
      $("#verifyResult").text("❌ 인증 실패. 다시 시도하세요.").removeClass("text-success").addClass("text-danger");
    }
  });
}

function changePassword(event) {
  event.preventDefault(); // 버튼 submit 방지
  const newPw = $("#newPassword").val();
  const email = $("#emailInput").val();

  $.post("${pageContext.request.contextPath}/AccountServlet", {
    action: "changePassword",
    email: email,
    newPassword: newPw
  }, function(response) {
    if (response.trim() === "success") {
      alert("비밀번호가 변경되었습니다. 다시 로그인해 주세요.");
      window.location.href = "${pageContext.request.contextPath}/user/logout";
    } else {
      alert("비밀번호 변경 실패");
    }
  });
}

$(document).on('input', '#newPassword, #confirmPassword', function () {
  const pw1 = $('#newPassword').val();
  const pw2 = $('#confirmPassword').val();
  const $msg = $('#passwordMatchMessage');

  if (!pw1 && !pw2) {
    $msg.text('');
    $('#changePasswordBtn').prop('disabled', true);
    return;
  }

  if (pw1 === pw2) {
    $msg.text('✅ 비밀번호가 일치합니다.').css('color', 'green');
    $('#changePasswordBtn').prop('disabled', false);
  } else {
    $msg.text('❌ 비밀번호가 일치하지 않습니다.').css('color', 'red');
    $('#changePasswordBtn').prop('disabled', true);
  }
});
</script>
	<!-- 회원탈퇴 모달 -->
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content p-3">
				<div class="modal-header">
					<h5 class="modal-title">회원 탈퇴</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>
						정말로 회원을 탈퇴하시겠습니까?<br>탈퇴 후에는 복구가 불가능합니다.
					</p>
					<div class="text-right">
						<button type="button" class="btn btn-danger"
							onclick="deleteAccount()">탈퇴</button>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
function deleteAccount() {
	  const email = $("#emailInput").val(); // 혹은 서버에서 user.id 사용도 가능

	  $.post("${pageContext.request.contextPath}/AccountServlet", {
	    action: "deleteAccount",
	    email: email
	  }, function (response) {
	    if (response.trim() === "success") {
	      alert("회원 탈퇴가 완료되었습니다.");
	      //로그인 세션제거
	      window.location.href = "${pageContext.request.contextPath}/";
	      
	    } else {
	      alert("회원 탈퇴에 실패했습니다.");
	    }
	  });
	}
</script>
</body>
</html>

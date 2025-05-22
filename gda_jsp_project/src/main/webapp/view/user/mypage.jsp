<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" />

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

/* 카드 스타일 */
.profile-card, .lecture-card {
	background: #fff;
	border-radius: 16px;
	box-shadow: 0 2px 8px #eee;
	padding: 20px;
	margin-bottom: 24px;
}

.lecture-card {
	display: flex;
	align-items: center;
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
	background-color: #e9ecef;
	border-radius: 4px;
	overflow: hidden;
}

.progress-bar {
	background: #6c6ce5;
	transition: width 0.6s ease;
}

.profile-form input, .profile-form select {
	margin-bottom: 16px;
}

.profile-form label {
	font-weight: 500;
}

/* 버튼 커스텀 hover 애니메이션 */
.btn-custom-primary {
	background-color: #6c6ce5;
	color: white;
	transition: background-color 0.3s ease, transform 0.2s ease;
	border: none;
}

.btn-custom-primary:hover, .btn-custom-primary:focus,
	.btn-custom-primary:active {
	background-color: #5757c7;
	color: white;
	transform: scale(1.05);
	box-shadow: 0 4px 12px rgba(108, 108, 229, 0.6);
	outline: none;
}

/* 이력서 보기 버튼 스타일 */
#viewResumeBtn {
	color: #6c6ce5;
	text-decoration: underline;
	background: none;
	border: none;
	cursor: pointer;
	font-weight: 500;
	transition: color 0.3s ease;
}

#viewResumeBtn:hover {
	color: #5757c7;
}

/* 회원탈퇴 링크 스타일 */
a.delete-link {
	color: #6c6ce5;
	font-size: 0.95rem;
	cursor: pointer;
	transition: color 0.3s ease;
}

a.delete-link:hover {
	color: #5757c7;
	text-decoration: underline;
}

/* 모달 버튼 스타일 통일 */
.modal .btn-danger, .modal .btn-secondary, .modal .btn-primary {
	transition: background-color 0.3s ease, transform 0.2s ease;
}

.modal .btn-danger:hover {
	background-color: #c82333;
	transform: scale(1.05);
}

.modal .btn-primary:hover {
	background-color: #5757c7;
	transform: scale(1.05);
}

.modal .btn-secondary:hover {
	background-color: #6c757d;
	transform: scale(1.05);
}

/* 비밀번호 일치 메시지 */
#passwordMatchMessage {
	font-weight: 500;
	margin-top: 6px;
}

/* 탭 메뉴 스타일 */
.mypage-left ul {
	display: flex;
	list-style: none;
	padding: 0;
	margin-bottom: 16px;
}

.mypage-left ul li {
	margin-right: 16px;
}

.mypage-left ul li a {
	text-decoration: none;
	color: #6c6ce5;
	font-weight: 600;
	cursor: pointer;
}

.mypage-left ul li a.active {
	border-bottom: 2px solid #6c6ce5;
	padding-bottom: 2px;
}

.lecture-card {
	margin-bottom: 12px;
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
						<c:when test="${user.role eq 'STUDENT'}">수강생</c:when>
						<c:when test="${user.role eq 'INSTRUCTOR'}">강사</c:when>
						<c:otherwise>네이버 유저</c:otherwise>
					</c:choose>
				</div>

				<!-- <a href="#"
					style="font-size: 0.95rem; color: #6c6ce5; text-decoration: underline;">수강강의
					전체</a> -->
				<ul class="nav nav-tabs" id="lectureTabs" role="tablist">
					<c:if test="${user.role == 'INSTRUCTOR'}">
						<li class="nav-item" role="presentation"><a
							class="nav-link active" id="myLectures-tab" data-toggle="tab"
							href="#myLectures" role="tab" aria-controls="myLectures"
							aria-selected="true">등록한 강의</a></li>
					</c:if>
					<li class="nav-item" role="presentation"><a
						class="nav-link <c:if test='${user.role != "INSTRUCTOR"}'>active</c:if>'"
						id="ongoingLectures-tab" data-toggle="tab" href="#ongoingLectures"
						role="tab" aria-controls="ongoingLectures" aria-selected="false">수강중인
							강의</a></li>
				</ul>

			</div>

			<div class="tab-content">
				<c:if test="${user.role == 'INSTRUCTOR'}">
					<div class="tab-pane fade show active" id="myLectures"
						role="tabpanel" aria-labelledby="myLectures-tab">

						<c:if test="${empty myLectures}">
							<p class="text-muted">등록한 강의가 없습니다.</p>
						</c:if>

						<c:forEach var="lecture" items="${myLectures}">
							<div class="lecture-card d-flex align-items-center">
								<img
									src="${pageContext.request.contextPath}${lecture.thumbnail}"
									class="lecture-img" alt="강의 이미지" />
								<div class="lecture-info">
									<div style="font-weight: 600;">${lecture.title}</div>
									<div class="lecture-instructor"
										style="font-size: 0.95rem; color: #888;">${lecture.description}</div>

									<div class="lecture-rating">
										<span class="star"><i class="fas fa-star"></i></span> <span
											class="rating-text">${lecture.avgRating}</span>
									</div>
								</div>

<a href="${pageContext.request.contextPath}/lecture/management" class="btn btn-primary">관리</a>

							</div>
						</c:forEach>
					</div>
				</c:if>

				<div
					class="tab-pane fade <c:if test='${user.role != "INSTRUCTOR"}'>show active</c:if>"
					id="ongoingLectures" role="tabpanel"
					aria-labelledby="ongoingLectures-tab">

					<c:if test="${empty myCourses}">
						<p class="text-muted">수강 중인 강의가 없습니다.</p>
					</c:if>

					<c:forEach var="course" items="${myCourses}">
						<div class="lecture-card d-flex align-items-center">
							<img src="${pageContext.request.contextPath}${course.thumbnail}"
								class="lecture-img" alt="강의 이미지" />
							<div class="lecture-info">
								<div style="font-weight: 600;">${course.title}</div>
								<div class="lecture-instructor"
									style="font-size: 0.95rem; color: #888;">${course.description}</div>

								<div class="lecture-rating">
									<span class="star"><i class="fas fa-star"></i></span> <span
										class="rating-text">${course.avgRating}</span>
								</div>
								<div class="progress" style="margin-top: 8px;">
									<div class="progress-bar"
										style="width: ${course.avgProgress}%; background: #6c6ce5;"></div>
								</div>

								<div style="font-size: 0.95rem;">진도율:
									${course.avgProgress}%</div>

							</div>

							<a
								href="${pageContext.request.contextPath}/lecture/play?lectureId=${course.lectureId}"
								class="btn btn-primary">수강</a>


						</div>
					</c:forEach>
				</div>
			</div>

		</div>

		<!-- 오른쪽 프로필 -->
		<div class="mypage-right">





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

			<c:if test="${user.role == 'INSTRUCTOR'}">
				<div class="resume-section" style="margin-top: 24px;">
					<h4>이력서</h4>
					<c:choose>
						<c:when test="${not empty user.resume}">
							<button id="viewResumeBtn" type="button"
								style="color: #6c6ce5; text-decoration: underline; background: none; border: none; cursor: pointer;">
								이력서 보기</button>
						</c:when>
						<c:otherwise>
							<p style="color: #888;">등록된 이력서가 없습니다.</p>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<c:choose>
				<c:when test="${user.isDeleted()}">
					<div class="text-right mt-2">
						<a href="#" data-toggle="modal" data-target="#cancelDeleteModal"
							style="color: #6c6ce5; font-size: 0.95rem;">탈퇴취소하기</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="text-right mt-2">
						<a href="#" data-toggle="modal" data-target="#deleteModal"
							style="color: #6c6ce5; font-size: 0.95rem;">탈퇴하기</a>
					</div>
				</c:otherwise>
			</c:choose>



			<!-- 	<div class="text-right mt-2">
				<a href="#" data-toggle="modal" data-target="#deleteModal"
					style="color: #6c6ce5; font-size: 0.95rem;">탈퇴하기</a>
			</div> -->


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
  $('#lectureTabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    var target = $(e.target).attr("href"); // activated tab-pane id
    $(target).addClass('animate__animated animate__fadeIn');
    
    // 애니메이션 클래스 제거 (재사용 위해)
    setTimeout(function() {
      $(target).removeClass('animate__animated animate__fadeIn');
    }, 1000);
  });
</script>
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
			  
			  $(document).ready(function(){
				  $("#viewResumeBtn").click(function(){
				    const email = "${user.email}";
				    $.post("${pageContext.request.contextPath}/AccountServlet", 
				      { action: "checkResume", email: email },
				      function(response) {
				        if(response.trim() === "exist") {
				          const downloadUrl = "${pageContext.request.contextPath}/AccountServlet?action=downloadResume&email=" + encodeURIComponent(email);
				          window.open(downloadUrl, "_blank");
				        } else {
				          alert("이력서 파일이 없습니다.");
				        }
				      });
				  });
				});

			  
	
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
	  
	  console.log("deleteAccount");

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

	<div class="modal fade" id="cancelDeleteModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content p-3">
				<div class="modal-header">
					<h5 class="modal-title">회원 탈퇴 취소</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>
						회원탈퇴 취소 하시겠습니까?
					</p>
					<div class="text-right">
						<button type="button" class="btn btn-danger"
							onclick="cancelDeleteAccount()">취소</button>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
	function cancelDeleteAccount() {
		  const userId = "${user.userId}" // 혹은 서버에서 user.id 사용도 가능

		  $.post("${pageContext.request.contextPath}/AccountServlet", {
		    action: "cancelDeleteAccount",
		    userId: userId
		  }, function (response) {
		    if (response.trim() === "success") {
		      alert("회원 탈퇴 취소가 완료되었습니다.");
		      window.location.href = "${pageContext.request.contextPath}/user/mypage";
		      
		    } else {
		      alert("회원 탈퇴취소에 실패했습니다.");
		    }
		  });
		}
	
	</script>



</body>
</html>
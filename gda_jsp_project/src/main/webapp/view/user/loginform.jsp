<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap"
	rel="stylesheet">
<!-- Bootstrap & FontAwesome -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<!-- jQuery & Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/userlogin.css">
</head>

<body>
	<div class="login-modal-custom position-relative">
		<a href="${pageContext.request.contextPath}/"
			class="home-btn position-absolute" style="top: 10px; left: 10px;">
			<i class="fas fa-home"></i> 홈
		</a>

		<div class="login-title">로그인</div>
		<div class="login-desc">당신의 계정에 접속하세요</div>

		<c:if test="${not empty loginError}">
			<div class="alert alert-danger">
				<c:out value="${loginError}" />
			</div>
		</c:if>

		<form id="loginForm"
			action="${pageContext.request.contextPath}/user/login" method="post"
			autocomplete="off">
			<div class="form-group">
				<label for="loginId" class="sr-only">아이디</label> <span
					class="input-icon"><i class="fas fa-user"></i></span> <input
					type="text" name="id" id="loginId" class="form-control"
					placeholder="아이디를 입력하세요" required />
			</div>

			<div class="form-group">
				<label for="loginPassword" class="sr-only">비밀번호</label> <span
					class="input-icon"><i class="fas fa-lock"></i></span> <input
					type="password" name="password" id="loginPassword"
					class="form-control" placeholder="비밀번호를 입력하세요" required />
			</div>

			<div class="login-links d-flex justify-content-between">
				<a href="#" class="find-id-btn">아이디 찾기</a> <a href="#"
					class="find-pw-btn">비밀번호 찾기</a>
			</div>

			<button type="submit" class="btn login-btn-main">로그인</button>

			<div class="or-divider">
				<span>or</span>
			</div>

			<button type="button" class="sns-btn sns-naver" id="naverLoginBtn">
				<strong>N</strong> Naver 로그인
			</button>
			<button type="button" class="sns-btn sns-kakao">
				<i class="fas fa-comment"></i> Kakao 로그인
			</button>
			<button type="button" class="sns-btn sns-google">
				<i class="fab fa-google"></i> Google 로그인
			</button>

			<div class="bottom-link mt-3">
				계정이 필요하신가요? <a
					href="${pageContext.request.contextPath}/user/signupform">회원가입</a>
			</div>
		</form>

		<!-- 아이디 찾기 모달 -->
		<div class="modal fade" id="findIdModal" tabindex="-1" role="dialog"
			aria-labelledby="findIdModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<form id="findIdForm" class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="findIdModalLabel">아이디 찾기</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="닫기">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label for="nameForFindId">이름</label> <input type="text"
								class="form-control" id="nameForFindId" name="name"
								placeholder="이름을 입력하세요" required />
						</div>
						<div class="form-group">
							<label for="birthForFindId">생년월일</label> <input type="date"
								class="form-control" id="birthForFindId" name="birth" required />
						</div>
						<div id="findIdResult" class="text-info mt-2"
							style="display: none;"></div>
					</div>
					<div class="modal-footer">
						<button type="submit" class="btn btn-primary">아이디 찾기</button>
					</div>
				</form>
			</div>
		</div>


		<!-- 비밀번호 찾기 모달 -->
		<div class="modal fade" id="findPwModal" tabindex="-1" role="dialog"
			aria-labelledby="findPwModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<form id="findPwForm" class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="findPwModalLabel">비밀번호 찾기</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="닫기">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label for="nameForId">이름</label> <input type="text"
								class="form-control" id="nameForId" name="name"
								placeholder="이름을 입력하세요" required />
						</div>
						<div class="form-group">
							<label for="emailForPw">가입한 이메일</label> <input type="email"
								class="form-control" id="emailForPw" name="email"
								placeholder="이메일을 입력하세요" required />
						</div>
						<div id="findPwResult" class="text-info mt-2"
							style="display: none;"></div>
					</div>
					<div class="modal-footer">
						<button type="submit" class="btn btn-primary">비밀번호 찾기</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- JavaScript -->
	<script>
    $(function () {
    
      $('#naverLoginBtn').on('click', function () {
        // 네이버 로그인 처리 로직
           // 네이버 개발자 센터에서 발급받은 Client ID와 Redirect URI를 사용하여 로그인 URL 생성
          const contextPath = '${pageContext.request.contextPath}'; // 컨텍스트 경로 가져오기
	
          const clientId = '13FTrHiwK64owp3t5Y4X'; // 네이버 개발자센터에서 발급받은 Client ID로 변경하세요
          const redirectURI = encodeURIComponent('http://localhost:8080' + contextPath + '/login/oauth2/code/naver');
          const state = Math.random().toString(36).substring(2, 15); // CSRF 방지를 위한 임의 문자열

          var naverAuthUrl = 'https://nid.naver.com/oauth2.0/authorize?response_type=code'
        	    + '&client_id=' + clientId
        	    + '&redirect_uri=' + redirectURI
        	    + '&state=' + state;

        	  window.location.href = naverAuthUrl;
      });
    	
    	
    	
      // 비밀번호 찾기 버튼 클릭 시 모달 표시
      $('.find-pw-btn').on('click', function (e) {
        e.preventDefault();
        $('#findPwModal').modal('show');
      });

      $('.find-id-btn').on('click', function (e) {
    	  e.preventDefault();
    	  $('#findIdModal').modal('show');
    	});
      
      
   // 아이디 찾기 버튼 클릭 이벤트
      $('.find-id-btn').on('click', function (e) {
        e.preventDefault();
        $('#findIdModal').modal('show');
      });

      // 아이디 찾기 폼 제출
      $('#findIdForm').on('submit', function (e) {
        e.preventDefault();

        const name = $('#nameForFindId').val();
        const birth = $('#birthForFindId').val();

        $.ajax({
          type: 'POST',
          url: '${pageContext.request.contextPath}/AccountServlet',
          data: {
            name: name,
            birth: birth,
            action: 'findId'
          },
          success: function (response) {
            if (response.trim() !== '') {
              $('#findIdModal').modal('hide');
              alert('찾은 아이디: ' + response.trim());
            } else {
              $('#findIdResult').text('입력한 정보와 일치하는 계정이 없습니다.').css('color', 'red').show();
            }
          },
          error: function () {
            $('#findIdResult').text('요청 처리 중 오류가 발생했습니다.').css('color', 'red').show();
          }
        });
      });
      
      
      
      // 비밀번호 찾기 폼 전송
      $('#findPwForm').on('submit', function (e) {
        e.preventDefault();

        const name = $('#nameForId').val().trim();
        const email = $('#emailForPw').val().trim();

        $.ajax({
          type: 'POST',
          url: '${pageContext.request.contextPath}/AccountServlet',
          data: { name, email, action: 'findPassword' },
          success: function (response) {
            const $result = $('#findPwResult');
            if (response.trim() === 'success') {
              //$result.text('임시 비밀번호가 이메일로 전송되었습니다.').css('color', 'green').show();
              $('#findPwModal').modal('hide');  // 모달 닫기
              alert('임시 비밀번호가 이메일로 전송되었습니다.');
            } else {
              $result.text('입력한 정보와 일치하는 계정이 없습니다.').css('color', 'red').show();
            }
          },
          error: function () {
            $('#findPwResult').text('요청 처리 중 오류가 발생했습니다.').css('color', 'red').show();
          }
        });
      });
    });
  </script>
</body>
</html>

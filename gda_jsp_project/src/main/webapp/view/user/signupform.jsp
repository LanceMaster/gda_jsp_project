<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>회원가입</title>

<!-- 외부 라이브러리 및 스타일 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/userlogin.css" />

<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

<style>
  body {
    background-color: #f8f9fa;
  }

  .login-modal-custom {
    max-width: 480px;
    margin: 40px auto;
    padding: 30px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  }

  .login-title {
    font-size: 1.8rem;
    font-weight: bold;
    text-align: center;
  }

  .login-desc {
    text-align: center;
    margin-bottom: 20px;
    color: #666;
  }

  .form-group {
    margin-bottom: 1rem;
  }

  .input-wrap {
    position: relative;
  }

  .input-icon {
    position: absolute;
    left: 10px;
    top: 50%;
    transform: translateY(-50%);
    color: #999;
  }

  .form-control {
    padding-left: 36px;
  }

  .btn-main {
    background-color: #6c6ce5;
    color: white;
    border: none;
    transition: background-color 0.3s ease;
  }

  .btn-main:hover {
    background-color: #5959c9;
  }

  .btn-disabled {
    background-color: #ccc !important;
    cursor: not-allowed !important;
    color: #666;
  }

  .btn-enabled {
    cursor: pointer !important;
  }

  #authSuccessMsg {
    color: green;
    font-weight: bold;
    margin-top: 10px;
    display: none;
  }

  .tab-btn {
    border: none;
    background: none;
    font-size: 1.1rem;
    font-weight: 600;
    padding: 6px 18px 8px 18px;
    color: #888;
    border-bottom: 2.5px solid transparent;
    transition: all 0.3s;
  }

  .tab-btn.active {
    color: #6c6ce5;
    border-bottom: 2.5px solid #6c6ce5;
  }

  .gap-2 > * + * {
    margin-left: 8px;
  }

  .home-btn {
    color: #555;
    font-size: 1.2rem;
  }

  .password-match-error {
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 5px;
    display: none;
  }

  .password-match-success {
    color: #28a745;
    font-size: 0.875rem;
    margin-top: 5px;
    display: none;
  }
</style>
</head>
<body>
<div class="login-modal-custom position-relative">
  <a href="${pageContext.request.contextPath}/" class="home-btn position-absolute" style="top: 10px; left: 10px;">
    <i class="fas fa-home"></i> 홈
  </a>

  <div class="login-title">수강생 회원가입</div>
  <div class="login-desc">회원 가입을 위해 정보를 입력하세요.</div>

  <div class="tab-bar d-flex justify-content-center mb-3">
    <button type="button" class="tab-btn active" data-role="STUDENT">수강생</button>
    <button type="button" class="tab-btn" data-role="INSTRUCTOR">강사</button>
  </div>

  <div class="email-guide text-center mb-2">이메일이 아이디로 사용됩니다.</div>

  <form id="signupForm" action="${pageContext.request.contextPath}/user/signup" method="post" enctype="multipart/form-data" autocomplete="off">
    <input type="hidden" name="role" id="roleInput" value="student" />

    <div class="form-group d-flex gap-2">
      <div class="input-wrap flex-fill position-relative">
        <span class="input-icon"><i class="fas fa-envelope"></i></span>
        <input type="email" name="email" id="emailInput" class="form-control" placeholder="이메일 주소 입력"
               required pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" />
      </div>
      <button type="button" class="btn btn-sm btn-main" id="emailDupCheckBtn">중복확인</button>
      <button type="button" class="btn btn-sm btn-main btn-disabled" id="emailVerifyBtn" disabled>인증요청</button>
    </div>

    <div class="form-group d-flex gap-2 mt-2" id="authCodeGroup" style="display: none;">
      <input type="text" id="authCodeInput" class="form-control" placeholder="인증코드 입력" required />
      <button type="button" class="btn btn-sm btn-main btn-disabled" id="authCodeCheckBtn" disabled>확인</button>
    </div>

    <div id="authSuccessMsg">이메일 인증이 완료되었습니다.</div>

    <div class="form-group">
      <span class="input-icon"><i class="fas fa-user"></i></span>
      <input type="text" name="name" class="form-control" placeholder="이름" required />
    </div>

    <div class="form-group">
      <span class="input-icon"><i class="fas fa-lock"></i></span>
      <input type="password" name="password" id="passwordInput" class="form-control" placeholder="비밀번호" required />
    </div>

    <div class="form-group">
      <span class="input-icon"><i class="fas fa-lock"></i></span>
      <input type="password" name="passwordCheck" id="passwordCheckInput" class="form-control" placeholder="비밀번호 확인" required />
      <div class="password-match-error" id="passwordError">비밀번호가 일치하지 않습니다.</div>
      <div class="password-match-success" id="passwordSuccess">비밀번호가 일치합니다.</div>
    </div>

    <div class="form-group">
      <span class="input-icon"><i class="fas fa-mobile-alt"></i></span>
      <input type="text" name="phone" class="form-control" placeholder="휴대폰 번호 (숫자만 입력)"
             required pattern="[0-9]{10,11}" title="숫자 10~11자리로 입력하세요." />
    </div>

    <div class="form-group">
      <span class="input-icon"><i class="fas fa-calendar-alt"></i></span>
      <input type="date" name="birthdate" class="form-control" required />
    </div>

    <div class="form-group" id="resumeGroup" style="display: none;">
      <span class="input-icon"><i class="fas fa-file-upload"></i></span>
      <input type="file" name="resume" id="resumeInput" class="form-control" accept=".pdf,.hwp,.doc,.docx" />
      <small class="form-text text-muted ml-2">강사는 이력서를 첨부해 주세요 (PDF, HWP, DOC, DOCX)</small>
    </div>

    <div class="form-group form-check">
      <input type="checkbox" name="agree" class="form-check-input" required />
      <label class="form-check-label">이용 약관에 동의합니다.</label>
    </div>

    <button type="submit" class="btn btn-main w-100 mt-2" disabled>회원가입</button>

    <div class="text-center mt-3">
      이미 회원이신가요? <a href="${pageContext.request.contextPath}/user/loginform">로그인</a>
    </div>
  </form>
</div>

<script>
$(function () {
  const setButtonState = (btn, enabled) => {
    btn.prop("disabled", !enabled)
      .toggleClass("btn-disabled", !enabled)
      .toggleClass("btn-enabled", enabled);
  };

  let emailConfirmed = false;
  let passwordsMatch = false;

  // 비밀번호 일치 확인 함수
  function checkPasswordMatch() {
    const password = $("#passwordInput").val();
    const passwordCheck = $("#passwordCheckInput").val();
    
    if (password && passwordCheck) {
      if (password === passwordCheck) {
        $("#passwordError").hide();
        $("#passwordSuccess").show();
        passwordsMatch = true;
      } else {
        $("#passwordError").show();
        $("#passwordSuccess").hide();
        passwordsMatch = false;
      }
    } else {
      $("#passwordError").hide();
      $("#passwordSuccess").hide();
      passwordsMatch = false;
    }
    
    checkFormValidity();
  }

  // 비밀번호 입력 필드에 이벤트 리스너 추가
  $("#passwordInput, #passwordCheckInput").on("input", checkPasswordMatch);

  $(".tab-btn").on("click", function () {
    const role = $(this).data("role");
    $("#roleInput").val(role);
    $(".tab-btn").removeClass("active");
    $(this).addClass("active");
    $("#resumeGroup").toggle(role === "INSTRUCTOR");
    checkFormValidity();
  });

  $("#emailDupCheckBtn").on("click", function () {
    const email = $("#emailInput").val().trim();
    if (!email || !/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/.test(email)) {
      alert("유효한 이메일 주소를 입력하세요.");
      return;
    }
    $.post("${pageContext.request.contextPath}/AccountServlet", { action: "emailDupCheck", email }, function (res) {
      if (res.trim() === "available") {
        alert("사용 가능한 이메일입니다.");
        setButtonState($("#emailDupCheckBtn"), false);
        setButtonState($("#emailVerifyBtn"), true);
        $("#emailInput").prop("readonly", true);
        emailConfirmed = true;
        checkFormValidity();
      } else {
        alert("이미 사용 중인 이메일입니다.");
      }
    });
  });

  $("#emailVerifyBtn").on("click", function () {
    const email = $("#emailInput").val().trim();
    $.post("${pageContext.request.contextPath}/AccountServlet", { action: "sendVerificationEmail", email }, function (res) {
      if (res.trim() === "sent") {
        alert("인증 이메일이 발송되었습니다.");
        $("#authCodeGroup").slideDown();
        setButtonState($("#authCodeCheckBtn"), true);
        setButtonState($("#emailVerifyBtn"), false);
      } else {
        alert("이메일 전송 실패: " + res);
      }
    });
  });

  $("#authCodeCheckBtn").on("click", function () {
    const code = $("#authCodeInput").val().trim();
    if (!code) {
      alert("인증코드를 입력하세요.");
      return;
    }
    $.post("${pageContext.request.contextPath}/AccountServlet", { action: "checkAuthCode", authCode: code }, function (res) {
      if (res.trim() === "success") {
        $("#authCodeGroup").slideUp();
        $("#authSuccessMsg").slideDown();
        setButtonState($("#authCodeCheckBtn"), false);
        checkFormValidity();
      } else {
        alert("인증코드가 일치하지 않습니다.");
      }
    });
  });

  $("#signupForm input, #signupForm select").on("input change", checkFormValidity);

  function checkFormValidity() {
    const allFilled = $("#signupForm")[0].checkValidity();
    const isEmailVerified = emailConfirmed && $("#authSuccessMsg").is(":visible");
    const isInstructor = $("#roleInput").val() === "INSTRUCTOR";
    const resumeOk = !isInstructor || $("#resumeInput").val();
    const agreed = $("input[name='agree']").is(":checked");

    // 비밀번호 일치 조건 추가
    $("button[type='submit']").prop("disabled", !(allFilled && isEmailVerified && resumeOk && agreed && passwordsMatch));
  }
});
</script>

</body>
</html>
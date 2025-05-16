<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>회원가입</title>

  <!-- Font Awesome 추가 (아이콘 표시용) -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/userlogin.css" />

  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

  <style>
    .btn-disabled {
      background-color: #ccc !important;
      cursor: not-allowed !important;
    }
    .btn-enabled {
      background-color: #6c6ce5 !important;
      cursor: pointer !important;
    }
    #authSuccessMsg {
      color: green;
      font-weight: bold;
      margin-top: 10px;
      display: none;
    }
  </style>
</head>
<body>
  <div class="login-modal-custom position-relative">
    <div class="popup-close-btn">&times;</div>
    <div class="login-title">수강생 회원가입</div>
    <div class="login-desc">회원 가입을 위해 정보를 입력하세요.</div>
    
    <div class="tab-bar" style="display: flex; gap: 24px; justify-content: center; margin-bottom: 18px;">
      <button type="button" class="tab-btn active" data-role="student"
        style="border: none; background: none; font-size: 1.1rem; font-weight: 600; color: #6c6ce5; border-bottom: 2.5px solid #6c6ce5; padding: 6px 18px 8px 18px;">수강생</button>
      <button type="button" class="tab-btn" data-role="teacher"
        style="border: none; background: none; font-size: 1.1rem; font-weight: 600; color: #888; border-bottom: 2.5px solid transparent; padding: 6px 18px 8px 18px;">강사</button>
    </div>

    <div class="email-guide">이메일이 아이디로 사용됩니다.</div>

    <form id="signupForm" action="${pageContext.request.contextPath}/user/signup" method="post" autocomplete="off" enctype="multipart/form-data">
      <input type="hidden" name="role" id="roleInput" value="student" />

      <!-- 이메일 + 인증 관련 -->
      <div class="form-group email-group" style="display:flex; gap:8px;">
        <div class="input-wrap" style="flex: 2; position: relative;">
          <span class="input-icon"><i class="fas fa-envelope"></i></span>
          <input
            type="email"
            name="email"
            id="emailInput"
            class="form-control"
            placeholder="이메일 주소를 입력하세요"
            required
            pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
            style="padding-left: 28px;"
          />
        </div>
        <button type="button" class="btn login-btn-main" id="emailDupCheckBtn" style="flex: 1;">중복확인</button>
        <button type="button" class="btn login-btn-main btn-disabled" id="emailVerifyBtn" style="flex: 1;" disabled>이메일 인증하기</button>
      </div>

      <div class="form-group" id="authCodeGroup" style="display: none; gap: 8px; margin-top: 10px; flex-wrap: nowrap; display:flex;">
        <input
          type="text"
          id="authCodeInput"
          class="form-control"
          placeholder="인증코드 입력"
          style="flex: 2;"
          required
        />
        <button type="button" class="btn login-btn-main btn-disabled" id="authCodeCheckBtn" style="flex: 1;" disabled>인증 확인</button>
      </div>

      <div id="authSuccessMsg">이메일 인증이 완료되었습니다.</div>

      <!-- 나머지 기본 정보 -->
      <div class="form-group" style="margin-top: 15px;">
        <span class="input-icon"><i class="fas fa-user"></i></span>
        <input type="text" name="name" class="form-control" placeholder="이름" required />
      </div>

      <div class="form-group">
        <span class="input-icon"><i class="fas fa-lock"></i></span>
        <input type="password" name="password" class="form-control" placeholder="비밀번호" required />
      </div>

      <div class="form-group">
        <span class="input-icon"><i class="fas fa-lock"></i></span>
        <input type="password" name="passwordCheck" class="form-control" placeholder="비밀번호 확인" required />
      </div>

      <div class="form-group">
        <span class="input-icon"><i class="fas fa-mobile-alt"></i></span>
        <input
          type="text"
          name="phone"
          class="form-control"
          placeholder="휴대폰 번호를 입력하세요"
          required
          pattern="[0-9]{10,11}"
          title="숫자 10~11자리로 입력하세요."
        />
      </div>

      <div class="form-group">
        <span class="input-icon"><i class="fas fa-calendar-alt"></i></span>
        <input type="date" name="birthdate" class="form-control" required />
      </div>

      <div class="gender-group" style="margin-top: 10px;">
        <label><input type="radio" name="gender" value="M" required /> 남</label>
        <label style="margin-left: 12px;"><input type="radio" name="gender" value="F" required /> 여</label>
      </div>

      <!-- 강사용 이력서 업로드 -->
      <div class="form-group" id="resumeGroup" style="display: none; margin-top: 15px;">
        <span class="input-icon"><i class="fas fa-file-upload"></i></span>
        <input
          type="file"
          name="resume"
          id="resumeInput"
          class="form-control"
          accept=".pdf,.hwp,.doc,.docx"
        />
        <span style="font-size: 0.98rem; color: #888; margin-left: 8px;">이력서 파일 (PDF, HWP, DOC, DOCX)</span>
      </div>

      <div class="agree-group" style="margin-top: 15px;">
        <label><input type="checkbox" name="agree" required /> 이용 약관에 동의합니다.</label>
      </div>

      <button type="submit" class="btn login-btn-main" style="margin-top: 20px;">회원가입</button>

      <div class="bottom-link" style="margin-top: 12px;">
        이미 회원이신가요? <a href="${pageContext.request.contextPath}/user/loginform" class="go-login">로그인</a>
      </div>
    </form>
  </div>

  <script>
    $(function () {
      // 탭 버튼 클릭 시 역할 변경, 이력서 업로드 표시 제어
      $(".tab-btn").on("click", function () {
        const role = $(this).data("role");
        $("#roleInput").val(role);
        $(".tab-btn").removeClass("active").css({ "border-bottom": "2.5px solid transparent", color: "#888" });
        $(this).addClass("active").css({ "border-bottom": "2.5px solid #6c6ce5", color: "#6c6ce5" });
        $("#resumeGroup").toggle(role === "teacher");
      });

      // 이메일 중복 확인
      $("#emailDupCheckBtn").on("click", function () {
        const email = $("#emailInput").val().trim();
        if (!email) return alert("이메일을 입력하세요.");

        $.post("${pageContext.request.contextPath}/AccountServlet", { action: "emailDupCheck", email }, function (res) {
          if (res.trim() === "available") {
            alert("사용 가능한 이메일입니다.");
            $("#emailInput").prop("readonly", true);
            $("#emailDupCheckBtn").prop("disabled", true).addClass("btn-disabled");
            $("#emailVerifyBtn").prop("disabled", false).removeClass("btn-disabled").addClass("btn-enabled");
          } else {
            alert("이미 사용 중인 이메일입니다.");
          }
        });
      });

      // 이메일 인증 요청
      $("#emailVerifyBtn").on("click", function () {
        const email = $("#emailInput").val().trim();
        $.post("${pageContext.request.contextPath}/AccountServlet", { action: "sendVerificationEmail", email }, function (res) {
          if (res.trim() === "sent") {
            alert("인증 이메일이 발송되었습니다.");
            $("#authCodeGroup").slideDown();
            $("#authCodeCheckBtn").prop("disabled", false).removeClass("btn-disabled").addClass("btn-enabled");
          } else {
            alert("이메일 전송 실패: " + res);
          }
        });
      });

      // 인증 코드 확인
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
            $("#authCodeCheckBtn").prop("disabled", true).addClass("btn-disabled").removeClass("btn-enabled");
            $("#emailVerifyBtn").prop("disabled", true).addClass("btn-disabled").removeClass("btn-enabled");
            $("#emailInput").prop("readonly", true); // 이메일 입력창도 잠금 처리 (보안 강화)
          } else {
            alert("인증코드가 일치하지 않습니다.");
          }
        });
      });

      // 비밀번호 일치 여부 확인
      $("#signupForm").on("submit", function (e) {
        var pw = $("input[name='password']").val();
        var pwCheck = $("input[name='passwordCheck']").val();
        if (pw !== pwCheck) {
          alert("비밀번호가 일치하지 않습니다.");
          e.preventDefault();
          return false;
        }
      });
    });
  </script>
</body>
</html>

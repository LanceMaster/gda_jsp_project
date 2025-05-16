<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- /webapp/layout/layout.jsp --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:set var="path" value="${pageContext.request.contextPath }"
	scope="application" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title><sitemesh:write property="title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500;700&display=swap" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/mainstyle.css" />
<c:if test="${not empty pageCss}">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}${pageCss}" />
</c:if>
<sitemesh:write property="head" />

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" />

<!-- JS: jQuery를 가장 먼저! -->
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>

<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="main-wrap">
		<!-- Header -->
		<div class="custom-header">
			<div class="logo-area">
				<!-- 예시 SVG 로고 -->
				<svg class="logo-img" viewBox="0 0 40 40" fill="none">
            <path d="M5 32L20 8L35 32H28L20 18L12 32H5Z" fill="#6C6CE5" />
          </svg>
				<a href="${path}/user/mainpage" class="logo-text"
					style="text-decoration: none; color: inherit">코딩스쿨</a>
			</div>
			<div class="nav-area">
				<a href="${path}/lecture/lecturelist" class="nav-link-custom">강의</a>

				<div class="dropdown d-inline">
					<a href="#" class="nav-link-custom dropdown-toggle"
						id="communityDropdown" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">커뮤니티</a>
					<div class="dropdown-menu" aria-labelledby="communityDropdown">
						<a class="dropdown-item" href="${path}/community/teamproject">팀
							프로젝트</a> <a class="dropdown-item" href="${path}/community/mentoring">멘토링</a>
					</div>
				</div>

				<%-- 로그인시 로그아웃보이고 로그인 안할시 로그인보이게 하기 --%>
				<c:if test="${empty sessionScope.user}">
					<button class="login-btn ml-3"
						onclick="location.href='${pageContext.request.contextPath}/user/loginform'">
						<i class="fas fa-sign-in-alt"></i> 로그인
					</button>
				</c:if>
				<!-- nav-area 내에 추가 -->
				<c:if test="${not empty sessionScope.user}">
					<div class="dropdown d-inline ml-3">
						<a href="#" class="nav-link-custom dropdown-toggle"
							id="profileDropdown" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false"> 프로필 </a>
						<div class="dropdown-menu" aria-labelledby="profileDropdown">
							<a class="dropdown-item" href="${path}/user/mypage">프로필 상세</a> <a
								class="dropdown-item" href="${path}/user/cart">장바구니</a> <a
								class="dropdown-item" href="${path}/user/logout">로그아웃</a>
						</div>
					</div>
				</c:if>
			</div>
			<!-- 로그인중 navbar -->
			<!-- jstl로 해야함 -->
		</div>

		<sitemesh:write property="body" />
</body>
</html>

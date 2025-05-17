<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%-- /webapp/layout/layout.jsp --%>
<c:set var="path" value="${pageContext.request.contextPath}"
	scope="application" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title><sitemesh:write property="title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- CSS Libraries -->
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

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" />

<sitemesh:write property="head" />

<!-- JS Libraries (순서 중요) -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.1/umd/popper.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="main-wrap">
		<!-- Header -->
		<div class="custom-header">
			<div class="logo-area">
				<svg class="logo-img" viewBox="0 0 40 40" fill="none">
					<path d="M5 32L20 8L35 32H28L20 18L12 32H5Z" fill="#6C6CE5" />
				</svg>
				<a href="${path}/user/mainpage" class="logo-text"
					style="text-decoration: none; color: inherit">코딩스쿨</a>
			</div>

			<div class="nav-area">
				<a href="${path}/lecture/lecturelist" class="nav-link-custom">강의</a>


	            <%-- 강의 등록 --%>
				<c:if
					test="${not empty sessionScope.user and sessionScope.user.role == 'INSTRUCTOR'}">
					<a href="${path}/lecture/lectureUpload" class="nav-link-custom">강의
						등록</a>
				</c:if>


				<div class="dropdown d-inline">
					<a href="#" class="nav-link-custom dropdown-toggle"
						id="communityDropdown" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">커뮤니티</a>
					<div class="dropdown-menu" aria-labelledby="communityDropdown">
						<a class="dropdown-item" href="${path}/community/teamproject">팀
							프로젝트</a> <a class="dropdown-item" href="${path}/community/mentoring">멘토링</a>
					</div>
				</div>

				<c:if test="${empty sessionScope.user}">
					<button class="login-btn ml-3"
						onclick="location.href='${pageContext.request.contextPath}/user/loginform'">
						<i class="fas fa-sign-in-alt"></i> 로그인
					</button>
				</c:if>

				<c:if test="${not empty sessionScope.user}">
					<div class="dropdown d-inline ml-3">
						<a href="#" class="nav-link-custom dropdown-toggle"
							id="profileDropdown" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">프로필</a>
						<div class="dropdown-menu" aria-labelledby="profileDropdown">
							<c:choose>
								<c:when test="${sessionScope.user.role == 'ADMIN'}">
									<a class="dropdown-item" href="${path}/admin/userlist">회원
										목록</a>
									<a class="dropdown-item" href="${path}/user/logout">로그아웃</a>
								</c:when>
								<c:otherwise>
									<a class="dropdown-item" href="${path}/user/mypage">프로필 상세</a>
									<a class="dropdown-item" href="${path}/lecture/cart">장바구니</a>
									<a class="dropdown-item" href="${path}/user/logout">로그아웃</a>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:if>
			</div>
		</div>

		<sitemesh:write property="body" />

		<!-- Footer -->
		<footer
			style="background-color: #fafafa; padding: 40px 0; border-top: 1px solid #ddd; font-size: 0.9rem; color: #666; margin-top: 100px;">
			<div
				class="main-wrap d-flex justify-content-between align-items-center flex-wrap"
				style="max-width: 1200px; margin: 0 auto; padding: 0 20px; border-bottom: 1px solid #ddd; padding-bottom: 16px; margin-bottom: 20px;">
				<div class="logo-area"
					style="display: flex; align-items: center; gap: 8px; font-weight: 700; font-size: 1.1rem; color: #333;">
					<svg class="logo-img" viewBox="0 0 40 40" fill="none"
						style="width: 28px; height: 28px;">
						<path d="M5 32L20 8L35 32H28L20 18L12 32H5Z" fill="#6C6CE5" />
					</svg>
					코딩스쿨
				</div>
				<nav class="nav-area"
					style="display: flex; gap: 24px; font-weight: 500;">
					<a href="${path}/company"
						style="color: #666; text-decoration: none;">회사 소개</a> <a
						href="${path}/contact" style="color: #666; text-decoration: none;">문의하기</a>
				</nav>
			</div>

			<div
				class="main-wrap d-flex justify-content-between align-items-center flex-wrap"
				style="max-width: 1200px; margin: 0 auto; padding: 0 20px;">
				<select class="form-control"
					style="width: 120px; background: #fff; border: 1px solid #ccc; color: #666; border-radius: 4px; padding: 6px 8px;">
					<option>한국어</option>
					<option>English</option>
					<option>日本語</option>
				</select>

				<div style="color: #999; font-size: 0.8rem;">
					© 2025 Brand, Inc. &nbsp;&nbsp;·&nbsp;&nbsp; <a
						href="${path}/privacy" style="color: #999; text-decoration: none;">Privacy</a>
					&nbsp;&nbsp;·&nbsp;&nbsp; <a href="${path}/terms"
						style="color: #999; text-decoration: none;">Terms</a>
					&nbsp;&nbsp;·&nbsp;&nbsp; <a href="${path}/sitemap"
						style="color: #999; text-decoration: none;">Sitemap</a>
				</div>

				<div class="social-icons"
					style="font-size: 1.3rem; display: flex; gap: 16px; color: #999;">
					<a href="https://twitter.com" target="_blank" style="color: #999;"><i
						class="fab fa-twitter"></i></a> <a href="https://facebook.com"
						target="_blank" style="color: #999;"><i
						class="fab fa-facebook-f"></i></a> <a href="https://linkedin.com"
						target="_blank" style="color: #999;"><i
						class="fab fa-linkedin-in"></i></a> <a href="https://youtube.com"
						target="_blank" style="color: #999;"><i class="fab fa-youtube"></i></a>
				</div>
			</div>
		</footer>
	</div>
</body>
</html>

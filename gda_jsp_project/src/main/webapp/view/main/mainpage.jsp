<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
	<!-- Tags -->
	<div class="mt-5 mb-5 d-flex flex-wrap justify-content-center">
		<button class="tag-btn">JavaScript</button>
		<button class="tag-btn">Spring Boot</button>
		<button class="tag-btn">Python</button>
		<button class="tag-btn">CSS</button>
		<button class="tag-btn">정보보안</button>
	</div>

	<!-- Search -->
	<div class="search-bar-area">
		<input type="text" class="search-bar"
			placeholder="검색하고 싶은 강의, 강사명을 입력해주세요" />
	</div>

	<!-- Carousel -->
	<section class="mb-5 mt-5">
		<div id="mainCarousel" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators">
				<li data-target="#mainCarousel" data-slide-to="0" class="active"></li>
				<li data-target="#mainCarousel" data-slide-to="1"></li>
				<li data-target="#mainCarousel" data-slide-to="2"></li>
			</ol>
			<div class="carousel-inner rounded">
				<div class="carousel-item active">
					<img src="testimage.png" class="d-block w-100" alt="배너1"
						style="height: 500px; object-fit: cover" />
				</div>
				<div class="carousel-item">
					<img src="testimage.png" class="d-block w-100" alt="배너2"
						style="height: 500px; object-fit: cover" />
				</div>
				<div class="carousel-item">
					<img src="testimage.png" class="d-block w-100" alt="배너3"
						style="height: 500px; object-fit: cover" />
				</div>
			</div>
			<a class="carousel-control-prev" href="#mainCarousel" role="button"
				data-slide="prev"> <span class="carousel-control-prev-icon"
				aria-hidden="true"></span> <span class="sr-only">이전</span>
			</a> <a class="carousel-control-next" href="#mainCarousel" role="button"
				data-slide="next"> <span class="carousel-control-next-icon"
				aria-hidden="true"></span> <span class="sr-only">다음</span>
			</a>
		</div>
	</section>

	<!-- 인기강의 -->
	<h3 class="font-weight-bold mb-4 mt-5">인기강의</h3>
	<div class="row">
		<!-- 강의 카드 반복 예시 -->
		<div class="col-md-3 mb-4">
			<div class="card lecture-card-custom h-100 shadow-sm border-0">
				<img src="testImage.png" class="card-img-top p-3" alt="강의 이미지"
					style="border-radius: 18px" />
				<div class="card-body text-center">
					<div class="font-weight-bold mb-2">
						[JavaScript] <span style="color: #f0b400">⭐ 4.7</span>
					</div>
					<div class="mb-2 text-muted" style="font-size: 0.97rem">
						모던 웹 개발 전격 정복<br />ES6부터 프론트엔드까지 실무 적용 핵심 기술 습득
					</div>
					<button class="btn btn-outline-primary btn-block"
						style="border-radius: 12px">수강 하러가기</button>
				</div>
			</div>
		</div>
		<!-- 필요시 col-md-3 카드 반복 -->
		<div class="col-md-3 mb-4">
			<div class="card lecture-card-custom h-100 shadow-sm border-0">
				<img src="testImage.png" class="card-img-top p-3" alt="강의 이미지"
					style="border-radius: 18px" />
				<div class="card-body text-center">
					<div class="font-weight-bold mb-2">
						[Spring Boot] <span style="color: #f0b400">⭐ 4.9</span>
					</div>
					<div class="mb-2 text-muted" style="font-size: 0.97rem">
						실전 프로젝트로 배우는 백엔드 API 개발<br />"Spring Boot + JPA + MySQL + 배포까지 한
						번에"
					</div>
					<button class="btn btn-outline-primary btn-block"
						style="border-radius: 12px">수강 하러가기</button>
				</div>
			</div>
		</div>
		<div class="col-md-3 mb-4">
			<div class="card lecture-card-custom h-100 shadow-sm border-0">
				<img src="testImage.png" class="card-img-top p-3" alt="강의 이미지"
					style="border-radius: 18px" />
				<div class="card-body text-center">
					<div class="font-weight-bold mb-2">
						[Python] <span style="color: #f0b400">⭐ 3.7</span>
					</div>
					<div class="mb-2 text-muted" style="font-size: 0.97rem">
						GPT API 활용한 AI 챗봇 만들기<br />"OpenAI GPT API를 이용한 실전형 챗봇 개발 가이드"
					</div>
					<button class="btn btn-outline-primary btn-block"
						style="border-radius: 12px">수강 하러가기</button>
				</div>
			</div>
		</div>
		<div class="col-md-3 mb-4">
			<div class="card lecture-card-custom h-100 shadow-sm border-0">
				<img src="testImage.png" class="card-img-top p-3" alt="강의 이미지"
					style="border-radius: 18px" />
				<div class="card-body text-center">
					<div class="font-weight-bold mb-2">
						[정보보안] <span style="color: #f0b400">⭐ 2.7</span>
					</div>
					<div class="mb-2 text-muted" style="font-size: 0.97rem">
						해킹 실전 분석과 대응<br />"XSS, SQL Injection, CSRF 등 보안 취약점 실습 기반 교육"
					</div>
					<button class="btn btn-outline-primary btn-block"
						style="border-radius: 12px">수강 하러가기</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS, Popper.js, jQuery (순서 중요) -->
	<!-- <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script> -->
	<!-- 	<script
		src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
 -->

</body>

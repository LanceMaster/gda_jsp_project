<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
	<!-- Tags -->
	<div class="mt-5 mb-5 d-flex flex-wrap justify-content-center">
		<a class="tag-btn"
			href="${pageContext.request.contextPath}/lecture/lecturelist?category=javascript">JavaScript</a>
		<a class="tag-btn"
			href="${pageContext.request.contextPath}/lecture/lecturelist?category=Spring+Boot">Spring
			Boot</a> <a class="tag-btn"
			href="${pageContext.request.contextPath}/lecture/lecturelist?category=python">Python</a>
		<a class="tag-btn"
			href="${pageContext.request.contextPath}/lecture/lecturelist?category=css">CSS</a>
		<a class="tag-btn"
			href="${pageContext.request.contextPath}/lecture/lecturelist?category=정보보안">정보보안</a>
	</div>

	<!-- Search -->
	<div class="search-bar-area">
		<form action="${pageContext.request.contextPath}/lecture/lecturelist"
			method="get"
			style="width: 100%; max-width: 1000px; display: flex; gap: 10px;">
			<input type="text" name="keyword" class="search-bar"
				placeholder="검색하고 싶은 강의, 강사명을 입력해주세요" />
			<button type="submit" class="btn btn-primary"
				style="border-radius: 25px; padding: 14px 28px;">검색</button>
		</form>
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
					<img
						src="${pageContext.request.contextPath}/static/images/thumbnail/thumbnail1.jpg"
						class="d-block w-100" alt="배너1"
						style="height: 500px; object-fit: cover" />
				</div>
				<div class="carousel-item">
					<img
						src="${pageContext.request.contextPath}/static/images/thumbnail/thumbnail2.jpg"
						class="d-block w-100" alt="배너2"
						style="height: 500px; object-fit: cover" />
				</div>
				<div class="carousel-item">
					<img
						src="${pageContext.request.contextPath}/static/images/thumbnail/thumbnail3.jpg"
						class="d-block w-100" alt="배너3"
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

	<h3 class="font-weight-bold mb-4 mt-5">인기강의</h3>
	<div class="row">
		<c:forEach var="lecture" items="${topLectures}">
			<div class="col-md-3 mb-4">
				<div class="card lecture-card-custom h-100 shadow-sm border-0">
					<img src="${pageContext.request.contextPath}${lecture.thumbnail}"
						class="card-img-top p-3" alt="강의 이미지" style="border-radius: 18px" />
					<div class="card-body text-center">
						<div class="font-weight-bold mb-2">
							[${lecture.category}] <span style="color: #f0b400">⭐
								${lecture.avgRating}</span>
						</div>
						<div class="mb-2 text-muted" style="font-size: 0.97rem">
							${lecture.title} <br /> ${lecture.description}
						</div>
						<button class="btn btn-outline-primary btn-block"
							style="border-radius: 12px">수강 하러가기</button>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<%-- <h3 class="font-weight-bold mb-4 mt-5">최신강의</h3>
	<div class="row">
		<c:forEach var="lecture" items="${newLectures}">
			<div class="col-md-3 mb-4">
				<div class="card lecture-card-custom h-100 shadow-sm border-0">
					<img src="${pageContext.request.contextPath}${lecture.thumbnail}"
						class="card-img-top p-3" alt="강의 이미지" style="border-radius: 18px" />
					<div class="card-body text-center">
						<div class="font-weight-bold mb-2">
							[${lecture.category}] <span style="color: #f0b400">⭐
								${lecture.avgRating}</span>
						</div>
						<div class="mb-2 text-muted" style="font-size: 0.97rem">
							${lecture.title} <br /> ${lecture.description}
						</div>
						<button class="btn btn-outline-primary btn-block"
							style="border-radius: 12px">수강 하러가기</button>
					</div>
				</div>
			</div>
		</c:forEach>
	</div> --%>


</body>
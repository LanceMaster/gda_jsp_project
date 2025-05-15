<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/projectsList.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


<meta charset="UTF-8" />
<title>프로젝트 목록</title>

	<div class="main-wrap">
		<h2 class="page-title">프로젝트 모집 게시판</h2>

		<!-- 검색/필터 Form 시작 -->
		<form method="get" action="list">
			<div class="search-bar">
				<input type="text" name="keyword" value="${param.keyword}"
					placeholder="검색어를 입력하세요" />
				<button type="submit">검색</button>
			</div>

			<div class="filter-bar">
				<select name="sort">
					<option value="recent" ${param.sort == 'recent' ? 'selected' : ''}>최신순</option>
					<option value="views" ${param.sort == 'views' ? 'selected' : ''}>조회순</option>
					<option value="popular"
						${param.sort == 'popular' ? 'selected' : ''}>인기순</option>
				</select> <select name="status">
					<option value="" ${empty param.status ? 'selected' : ''}>전체
						상태</option>
					<option value="RECRUITING"
						${param.status == 'RECRUITING' ? 'selected' : ''}>모집중</option>
					<option value="CLOSED"
						${param.status == 'CLOSED' ? 'selected' : ''}>모집완료</option>
				</select>
				<button type="submit" class="action-btn">적용</button>
				<button type="button" onclick="location.href='projectsForm'"
					class="action-btn">글쓰기</button>
			</div>
		</form>
		<!-- 검색/필터 Form 끝 -->

		<!-- 프로젝트 카드 목록 -->
		<div class="project-list">
			<c:forEach var="p" items="${projects}">
				<div class="project-card">
					<div class="card-header">
						<span class="badge"> <c:choose>
								<c:when test="${p.recruitStatus == 'RECRUITING'}">모집중</c:when>
								<c:otherwise>모집완료</c:otherwise>
							</c:choose>
						</span>
						<h3>
							<a href="info?projectId=${p.projectId}">${p.title}</a>
						</h3>
					</div>
					    <!-- ✅ 내용 출력 추가 시작 -->
                           <div class="card-content">
                           ${p.description}
                           </div>
                        <!-- ✅ 내용 출력 추가 끝 -->
					<div class="card-meta">
						<span>작성자: ${p.leaderId}</span> | <span>등록일: ${p.createdAt}</span>
						| <span>조회수: ${p.viewCount}</span>
					</div>
				</div>
			</c:forEach>
		</div>

		<!-- ✅ 방어 처리된 페이징 영역 시작 -->
		<div class="pagination">
			<c:if test="${pageInfo.hasPreviousPage}">
				<a
					href="?pageNum=${pageInfo.prePage}&keyword=${param.keyword}&sort=${param.sort}&status=${param.status}">&lt;
					이전</a>
			</c:if>

			<c:choose>
				<c:when test="${pageInfo.pages > 0}">
					<c:forEach begin="1" end="${pageInfo.pages}" var="i">
						<a
							href="?pageNum=${i}&keyword=${param.keyword}&sort=${param.sort}&status=${param.status}"
							class="${pageInfo.pageNum == i ? 'active' : ''}">${i}</a>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<a href="?pageNum=1" class="active">1</a>
				</c:otherwise>
			</c:choose>

			<c:if test="${pageInfo.hasNextPage}">
				<a
					href="?pageNum=${pageInfo.nextPage}&keyword=${param.keyword}&sort=${param.sort}&status=${param.status}">다음
					&gt;</a>
			</c:if>
		</div>
		<!-- ✅ 방어 처리된 페이징 영역 끝 -->

	</div>

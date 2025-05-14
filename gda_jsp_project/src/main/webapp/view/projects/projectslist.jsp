<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>프로젝트 목록</title>

  <!-- ✅ 직접 삽입한 스타일 -->
  <style>
    .main-wrap { max-width: 1200px; margin: auto; }
    .page-title { font-size: 24px; font-weight: bold; margin: 20px 0; }
    .search-bar, .filter-bar { margin-bottom: 15px; display: flex; gap: 10px; }
    .project-list { display: flex; flex-wrap: wrap; gap: 20px; }
    .project-card { border: 1px solid #ddd; padding: 15px; width: calc(33% - 20px); box-shadow: 2px 2px 5px rgba(0,0,0,0.1); }
    .project-card h3 { margin-top: 0; }
    .badge { background: #28a745; color: #fff; padding: 2px 8px; border-radius: 4px; }
    .pagination { margin-top: 20px; display: flex; justify-content: center; gap: 5px; }
    .pagination button { padding: 5px 10px; }
    .pagination .active { font-weight: bold; background: #eee; }
  </style>

</head>
<body>
<div class="main-wrap">
  <h2 class="page-title">프로젝트 모집 게시판</h2>

  <!-- 검색/필터 Form 시작 -->
  <form method="get" action="list">
    <div class="search-bar">
      <input type="text" name="keyword" value="${param.keyword}" placeholder="검색어를 입력하세요" />
      <button type="submit">검색</button>
    </div>

    <div class="filter-bar">
      <select name="sort">
        <option value="recent" ${param.sort == 'recent' ? 'selected' : ''}>최신순</option>
        <option value="views" ${param.sort == 'views' ? 'selected' : ''}>조회순</option>
        <option value="popular" ${param.sort == 'popular' ? 'selected' : ''}>인기순</option>
      </select>
      <select name="status">
        <option value="" ${empty param.status ? 'selected' : ''}>전체 상태</option>
        <option value="RECRUITING" ${param.status == 'RECRUITING' ? 'selected' : ''}>모집중</option>
        <option value="CLOSED" ${param.status == 'CLOSED' ? 'selected' : ''}>모집완료</option>
      </select>
      <button type="submit" class="action-btn">적용</button>
      <button type="button" onclick="location.href='writeForm'" class="action-btn">글쓰기</button>
    </div>
  </form>
  <!-- 검색/필터 Form 끝 -->

  <!-- 프로젝트 카드 목록 -->
  <div class="project-list">
    <c:forEach var="p" items="${projects}">
      <div class="project-card">
        <div class="card-header">
          <span class="badge">
            <c:choose>
              <c:when test="${p.recruitStatus == 'RECRUITING'}">모집중</c:when>
              <c:otherwise>모집완료</c:otherwise>
            </c:choose>
          </span>
          <h3>
            <a href="info?projectId=${p.projectId}">${p.title}</a>
          </h3>
        </div>
        <div class="card-meta">
          <span>작성자: ${p.leaderId}</span> |
          <span>등록일: ${p.createdAt}</span> |
          <span>조회수: ${p.viewCount}</span>
        </div>
      </div>
    </c:forEach>
  </div>

  <!-- 페이징 영역 -->
  <div class="pagination">
    <button>&lt; 이전 페이지</button>
    <button class="active">1</button>
    <button>2</button>
    <button>3</button>
    <button>다음 페이지 &gt;</button>
  </div>
</div>
</body>
</html>

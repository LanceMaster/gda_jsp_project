<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="<c:url value='/static/css/lectureList.css' />" />
<script src="<c:url value='/static/js/lectureList.js' />"></script>

<h1>강의 목록</h1>

<!-- ✅ 추천 강의 섹션 -->
<c:if test="${not empty recommendedLectures}">
  <div class="recommended-section">
    <h2>🔥 추천 강의</h2>
    <ul>
      <c:forEach var="rec" items="${recommendedLectures}">
        <li>
          <a href="<c:url value='/lecture/lecturedetail?lectureId=${rec.lectureId}' />">
            ${rec.title} - ⭐ ${rec.avgRating} (${rec.reviewCount})
          </a>
        </li>
      </c:forEach>
    </ul>
  </div>
</c:if>

<!-- ✅ 인기 태그 -->
<div class="top-tags">
  <form method="get" action="/lecture/lecturelist">
    <c:forEach var="tag" items="${topTags}">
      <button class="tag-btn" name="keyword" value="${tag.name}">${tag.name}</button>
    </c:forEach>
  </form>
</div>

<!-- ✅ 카테고리 필터 -->
<div class="category-buttons">
  <form method="get" action="/lecture/category">
    <button class="category-btn" name="category" value="javascript">JavaScript</button>
    <button class="category-btn" name="category" value="SpringBoot">Spring Boot</button>
    <button class="category-btn" name="category" value="Python">Python</button>
    <button class="category-btn" name="category" value="CSS">CSS</button>
    <button class="category-btn" name="category" value="정보보안">정보보안</button>
    <button class="category-btn" name="category" value="백엔드">백엔드</button>
  </form>
</div>

<!-- ✅ 검색창 -->
<div class="search-box">
  <form method="get" action="/lecture/lecturelist" class="search-form">
    <input type="text" name="keyword" value="${param.keyword}" placeholder="🔍 강의, 강사명을 입력하세요" class="search-input" />
    <input type="hidden" name="category" value="${param.category}" />
    <input type="hidden" name="sort" value="${param.sort}" />
    <button type="submit" class="search-btn">검색</button>
  </form>
</div>

<!-- ✅ 정렬 드롭다운 -->
<div class="sort-dropdown">
  <form method="get" action="/lecture/lecturelist">
    <input type="hidden" name="category" value="${param.category}" />
    <input type="hidden" name="keyword" value="${param.keyword}" />
    <label for="sort-select">정렬:</label>
    <select name="sort" id="sort-select" onchange="this.form.submit()">
      <option value="latest" ${param.sort == 'latest' || param.sort == null ? 'selected' : ''}>🆕 최신순</option>
      <option value="popular" ${param.sort == 'popular' ? 'selected' : ''}>🔥 인기순</option>
    </select>
  </form>
</div>

<!-- ✅ 강의 목록 출력 -->
<div class="lecture-list-wrapper">
<div class="lecture-list">
  <c:forEach var="lec" items="${lectures}">
    <div class="lecture-card">
      <a href="<c:url value='/lecture/lecturedetail?lectureId=${lec.lectureId}' />">
        <img src="<c:url value='${lec.thumbnail}' />" alt="${lec.title}" />
        <h4>${lec.title}</h4>
        <p><fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" /></p>
        <p class="rating">⭐ ${lec.avgRating != null ? lec.avgRating : 0} <span class="review-count">(${lec.reviewCount})</span></p>
      </a>
      <button type="button" class="add-cart-btn" onclick="addToCart(${lec.lectureId})">장바구니에 담기</button>
    </div>
  </c:forEach>
</div>
</div>

<!-- ✅ 페이지네이션 -->
<div class="pagination">
  <c:if test="${page > 1}">
    <a href="?page=${page - 1}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}">&lt; 이전</a>
  </c:if>

  <c:forEach begin="1" end="${Math.ceil(totalCount / size)}" var="i">
    <a href="?page=${i}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}" class="${i == page ? 'active' : ''}">${i}</a>
  </c:forEach>

  <c:if test="${page * size < totalCount}">
    <a href="?page=${page + 1}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}">다음 &gt;</a>
  </c:if>
</div>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="<c:url value='/static/css/lectureList.css' />" />

<h1>강의 목록</h1>

<!-- ✅ 카테고리 버튼 -->
<div class="category-buttons">
  <form method="get" action="/lecture/lectureList">
    <button class="category-btn" name="category" value="JavaScript">JavaScript</button>
    <button class="category-btn" name="category" value="Spring Boot">Spring Boot</button>
    <button class="category-btn" name="category" value="Python">Python</button>
    <button class="category-btn" name="category" value="CSS">CSS</button>
    <button class="category-btn" name="category" value="정보보안">정보보안</button>
    <button class="category-btn" name="category" value="정보보안">백엔드</button>
  </form>
</div>

<!-- ✅ 검색창 -->
<div class="search-box">
  <form method="get" action="/lecture/lectureList" class="search-form">
    <input
      type="text"
      name="keyword"
      value="${param.keyword}"
      placeholder="🔍 강의, 강사명을 입력하세요"
      class="search-input"
    />
    <input type="hidden" name="category" value="${param.category}" />
    <input type="hidden" name="sort" value="${param.sort}" />
    <button type="submit" class="search-btn">검색</button>
  </form>
</div>




<!-- ✅ 정렬 드롭다운 -->
<div class="sort-dropdown">
  <form method="get" action="/lecture/lectureList">
    <input type="hidden" name="category" value="${param.category}" />
    <label for="sort-select"></label>
    <select name="sort" id="sort-select" onchange="this.form.submit()">
      <option value="latest" ${param.sort == 'latest' || param.sort == null ? 'selected' : ''}>🆕 최신순</option>
      <option value="popular" ${param.sort == 'popular' ? 'selected' : ''}>🔥 인기순</option>
    </select>
  </form>
</div>




<!-- ✅ 강의 목록 출력 -->
<div class="lecture-list">
  <c:forEach var="lec" items="${lectures}">
    <div class="lecture-card">

      <!-- ✅ 상세 페이지로 이동 -->
		<a href="<c:url value='/lecture/lecturedetail?lectureId=${lec.lectureId}' />">
        <img src="<c:url value='${lec.thumbnail}' />" alt="${lec.title}" />
        <h4><c:out value="${lec.description}" /></h4>
      </a>

      <!-- ✅ 가격 및 평점 (null 방어 포함) -->
      <p>
        <fmt:formatNumber value="${lec.price != null ? lec.price : 0}" type="currency" currencySymbol="₩" />
      </p>
      
<p class="rating">
  ⭐ <c:out value="${lec.avgRating != null ? lec.avgRating : 0}" />
  <span class="review-count">(<c:out value="${lec.reviewCount}" />)</span>
</p>

<!--  <p>⭐ <c:out value="${lec.avgRating != null ? lec.avgRating : 0}" /></p> -->

      <!-- ✅ 장바구니 담기 폼 -->
      <form method="post" action="/cart/add">
        <input type="hidden" name="lectureId" value="${lec.lectureId}" />
        <button type="submit" class="add-cart-btn">장바구니에 담기</button>
      </form>

    </div>
  </c:forEach>
</div>

<!-- ✅ 페이지네이션 (연동은 나중에 처리 가능) -->
<div class="pagination">
  <button disabled>&lt; 이전 페이지</button>
  <button class="active">1</button>
  <button>2</button>
  <button>3</button>
  <button>4</button>
  <button>다음 페이지 &gt;</button>
</div>

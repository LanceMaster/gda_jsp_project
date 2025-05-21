<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/lectureList.css" />
<script src="<c:url value='/static/js/lectureList.js' />"></script>

<h1>강의 목록</h1>
<c:url var="addCartUrl" value="/cart/add" />
<!-- ✅ 하나의 필터 통합 form + 안전한 action 경로 -->
<form method="get" action="${pageContext.request.contextPath}/lecture/lecturelist" class="filter-form">

  <!-- ✅ 인기 태그 -->
  <div class="top-tags">
    <c:forEach var="tag" items="${topTags}">
      <button type="submit" class="tag-btn" name="keyword" value="${tag.name}">${tag.name}</button>
    </c:forEach>
  </div>

  <!-- ✅ 카테고리 필터 -->
  <div class="category-buttons">
    <button type="submit" class="category-btn" name="category" value="javascript">JavaScript</button>
    <button type="submit" class="category-btn" name="category" value="Spring Boot">Spring Boot</button>
    <button type="submit" class="category-btn" name="category" value="Python">Python</button>
    <button type="submit" class="category-btn" name="category" value="CSS">CSS</button>
    <button type="submit" class="category-btn" name="category" value="정보보안">정보보안</button>
    <button type="submit" class="category-btn" name="category" value="백엔드">백엔드</button>
  </div>

  <!-- ✅ 검색창 -->
  <div class="search-box">
    <input type="text" name="keyword" value="${param.keyword}" placeholder="    강의, 카테고리를 입력하세요" class="search-input" />
    <button type="submit" class="search-btn">검색</button>
  </div>
  

  <!-- ✅ 정렬 드롭다운 -->
  <div class="sort-dropdown">
    <label for="sort-select">정렬:</label>
    <select name="sort" id="sort-select" onchange="this.form.submit()">
      <option value="latest" <c:if test="${param.sort == 'latest' || empty param.sort}">selected</c:if>>최신순</option>
      <option value="popular" <c:if test="${param.sort == 'popular'}">selected</c:if>>인기순</option>
    </select>
  </div>
</form>


<!-- ✅ 최대 8개까지만 출력 -->
<div class="lecture-list-wrapper">
  <div class="lecture-list">
    <c:forEach var="lec" items="${lectures}" varStatus="status">
      <c:if test="${status.index < 8}">
        <div class="lecture-card">
          <a href="<c:url value='/lecture/lecturedetail?lectureId=${lec.lectureId}' />">
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 썸네일 이미지 출력 -->
<img src="${lec.thumbnail}" alt="${lec.title}" style="width:300px;" />
            <h4>${lec.title}</h4>
            <p><fmt:formatNumber value="${lec.price}" type="currency" currencySymbol="₩" /></p>
       <!--  아무거나 -->    
            <p class="rating">
              ⭐ <c:choose>
                <c:when test="${lec.avgRating != null}">
                  <fmt:formatNumber value="${lec.avgRating}" type="number" maxFractionDigits="1" />
                </c:when>
                <c:otherwise>0.0</c:otherwise>
              </c:choose>
              <span class="review-count">
                (<c:choose>
                  <c:when test="${lec.reviewCount != null}">
                    ${lec.reviewCount}
                  </c:when>
                  <c:otherwise>0</c:otherwise>
                </c:choose>)
              </span>
            </p>
          </a>
           <button type="button" class="add-cart-btn" onclick="addToCart(${lec.lectureId})">장바구니에 담기</button>
        </div>
      </c:if>
    </c:forEach>
  </div>
</div>



<!-- ✅ 총 페이지 수 계산 (올림 처리) -->
<c:set var="totalPages" value="${(totalCount % size == 0) ? (totalCount / size) : (totalCount / size + 1)}" />

<!-- ✅ 페이지네이션 -->
<div class="pagination">
  <c:if test="${page > 1}">
    <a href="?page=${page - 1}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}">&lt; 이전</a>
  </c:if>

  <c:forEach begin="1" end="${totalPages}" var="i">
    <a href="?page=${i}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}"
       class="<c:if test='${i == page}'>active</c:if>">${i}</a>
  </c:forEach>

  <c:if test="${page * size < totalCount}">
    <a href="?page=${page + 1}&category=${param.category}&keyword=${param.keyword}&sort=${param.sort}">다음 &gt;</a>
  </c:if>
</div>

<script>
  const addCartUrl = '${addCartUrl}';
</script>

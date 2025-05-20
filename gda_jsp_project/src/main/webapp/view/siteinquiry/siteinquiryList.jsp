<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<meta charset="UTF-8">
<title>사이트 문의 게시판</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/siteinquiryList.css" />

<div class="siteinquiryList-wrap">

    <!-- 에러 메시지 출력 -->
    <c:if test="${not empty errorMessage}">
        <div class="siteinquiryList-error">
            ${errorMessage}
        </div>
    </c:if>

    <!-- 검색 및 작성 -->
<form class="siteinquiryList-search" method="get" action="list">
    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
        <button type="submit" name="filterUnanswered" value="true" class="siteinquiryList-search-btn" title="미답변 정렬">
            미답변 정렬
        </button>
    </c:if>
    <input type="text" name="keyword" value="${fn:escapeXml(param.keyword)}" class="siteinquiryList-search-input"
           placeholder="검색하고 싶은 문의를 입력해주세요" />
    <button type="submit" class="siteinquiryList-search-btn" title="검색">
        <span class="search-btn-icon">🔍</span> 검색
    </button>
    <a href="writeForm" class="siteinquiryList-write-btn">문의글 작성하기</a>
</form>


    <!-- 테이블 -->
    <div class="siteinquiryList-table-container">
        <table class="siteinquiryList-table">
            <thead>
                <tr>
                    <th>글 번호</th>
                    <th>제목</th>
                    <th>문의내역</th>
                    <th class="siteinquiryList-actions-header">Actions</th>
                </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty inquiries}">
                    <c:forEach var="inquiry" items="${inquiries}">
                        <tr>
                            <td>
                                <fmt:formatNumber value="${inquiry.inquiryId}" pattern="000" />
                            </td>
                            <td>${inquiry.title}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:length(inquiry.content) > 30}">
                                        ${fn:substring(inquiry.content, 0, 30)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${inquiry.content}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                           <div class="siteinquiryList-actions">
                   <a href="detail?inquiryId=${inquiry.inquiryId}" class="siteinquiryList-detail-btn">상세보기</a>
            <c:if test="${inquiry.answered}">
             <span class="siteinquiryList-answer-btn">답변완료</span>
            </c:if>
                    </div>
                       </td>

                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4" class="siteinquiryList-no-data">등록된 문의글이 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <!-- 페이징 -->
    <div class="pagination">
        <c:if test="${pageInfo.hasPreviousPage}">
            <a href="?pageNum=${pageInfo.prePage}">&lt; 이전</a>
        </c:if>

        <c:choose>
            <c:when test="${pageInfo.pages > 0}">
                <c:forEach begin="1" end="${pageInfo.pages}" var="i">
                    <a href="?pageNum=${i}" class="${pageInfo.pageNum == i ? 'active' : ''}">${i}</a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <a href="?pageNum=1" class="active">1</a>
            </c:otherwise>
        </c:choose>

        <c:if test="${pageInfo.hasNextPage}">
            <a href="?pageNum=${pageInfo.nextPage}">다음 &gt;</a>
        </c:if>
    </div>
<!-- ✅ 뒤로 가기 캐시 새로고침 스크립트 개선 -->
<script>
    window.addEventListener('pageshow', function(event) {
        if (event.persisted || (window.performance && window.performance.navigation.type === 2)) {
            window.location.reload();
        }
    });
</script>
</div>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

    <meta charset="UTF-8">
    <title>${inquiry.title} - 문의 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/siteinquiryDetail.css" />

<div class="inquiry-container">

    <div class="inquiry-header">
        <span class="inquiry-title">${inquiry.title}</span>
            <div class="action-buttons">
        <c:if test="${loginUserId eq inquiry.userId}">
                <a href="${pageContext.request.contextPath}/siteinquiry/editForm?inquiryId=${inquiry.inquiryId}" class="edit-link">수정하기</a>
                <a href="${pageContext.request.contextPath}/siteinquiry/delete?inquiryId=${inquiry.inquiryId}" 
                   class="delete-link" 
                   onclick="return confirm('정말 삭제하시겠습니까?');">삭제하기</a>
        </c:if>
                <a href="${pageContext.request.contextPath}/siteinquiry/list" class="btn-list">목록으로</a>            
            </div>
    </div>

    <div class="inquiry-meta">
        작성자: ${inquiry.userName} | 등록일: 
        <fmt:formatDate value="${inquiry.createdAt}" pattern="yyyy-MM-dd" />
        <br>답변 여부: 
        <c:choose>
            <c:when test="${inquiry.answered}">
                답변 완료
            </c:when>
            <c:otherwise>
                미답변
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 문의 내용 -->
    <div class="inquiry-content">
        <c:out value="${inquiry.content}" escapeXml="false" />
    </div>

    <!-- 댓글 입력 (ADMIN 사용자만) -->
    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
        <div class="comment-box">
            <form method="post" action="${pageContext.request.contextPath}/siteinquiry/addComment">
                <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />
                <input type="text" name="commentContent" class="comment-input" placeholder="댓글을 입력해 주세요." required />
                <button type="submit" class="comment-submit">제출</button>
            </form>
        </div>
    </c:if>

    <!-- 댓글 목록 -->
    <div class="comment-list">
        <c:forEach var="comment" items="${comments}">
            <div class="comment-item">
                <strong>${comment.userName}</strong><br/>
                ${comment.content}
                <c:if test="${loginUserId == comment.userId}">
                    <form method="post" action="${pageContext.request.contextPath}/siteinquiry/deleteComment" style="display:inline;">
                        <input type="hidden" name="commentId" value="${comment.commentId}" />
                        <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />
                        <button type="submit" class="comment-delete-button">삭제</button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>

</div>


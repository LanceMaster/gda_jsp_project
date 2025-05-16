<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageCss" value="/static/css/projectsDetail.css" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${project.title} - 프로젝트 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}${pageCss}">
</head>
<body>
<div class="project-container">

    <!-- 제목 + 수정 링크 -->
    <div class="project-header">
        ${project.title}
        <a href="${pageContext.request.contextPath}/projects/projectsEdit?projectId=${project.projectId}" class="edit-link">수정하기</a>
    </div>

    <!-- 작성자, 등록일, 조회수 -->
    <div class="project-meta">
        ${project.leaderName} | 게시물등록일 :
        <fmt:formatDate value="${project.createdAt}" pattern="yyyy-MM-dd" />
        <br> 조회수 ${project.viewCount} Views
    </div>

    <!-- 태그 출력 -->
    <div>
        <c:forEach var="tag" items="${projectTags}">
            <span class="tag">${tag.name}</span>
        </c:forEach>
    </div>

    <!-- 본문 내용 (Summernote HTML 지원) -->
    <div class="project-description">
        <c:out value="${project.description}" escapeXml="false" />
    </div>

    <!-- 댓글 입력 -->
    <div class="comment-box">
        <form method="post" action="${pageContext.request.contextPath}/projects/addComment">
            <input type="hidden" name="projectId" value="${project.projectId}" />
            <input type="text" name="commentContent" class="comment-input" placeholder="댓글을 입력해 주세요." required />
            <button type="submit" class="comment-submit">제출</button>
        </form>
    </div>

    <!-- 댓글 목록 -->
    <div class="comment-list">
        <c:forEach var="comment" items="${comments}">
            <div class="comment-item">
                <strong>${comment.userId}</strong> <br/>
                ${comment.content}
                <form method="post" action="${pageContext.request.contextPath}/projects/deleteComment" style="display:inline;">
                    <input type="hidden" name="commentId" value="${comment.commentId}" />
                    <input type="hidden" name="projectId" value="${project.projectId}" />
                    <button type="submit" class="comment-delete-button">삭제</button>
                </form>
            </div>
        </c:forEach>
    </div>

</div>
</body>
</html>

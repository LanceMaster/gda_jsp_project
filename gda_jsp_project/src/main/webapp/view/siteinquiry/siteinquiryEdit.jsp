<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>문의글 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/siteinquiryEdit.css" />
</head>
<body>
    <div class="form-container">
        <h2>문의글 수정</h2>
        <form method="post" action="edit">
            <!-- 수정할 문의글 ID를 숨겨서 전달 -->
            <input type="hidden" name="inquiryId" value="${inquiry.inquiryId}" />

            <!-- 기존 제목을 value에 세팅 -->
            <input type="text" name="title" placeholder="제목" maxlength="200" required 
                   value="${inquiry.title}" />

            <!-- 기존 내용을 textarea에 세팅 -->
            <textarea name="content" placeholder="문의 내용을 작성해주세요" required>${inquiry.content}</textarea>

            <div class="form-actions">
                <a href="list">취소</a>
                <button type="submit">수정</button>
            </div>
        </form>
    </div>
</body>
</html>

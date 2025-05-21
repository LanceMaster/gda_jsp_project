<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>문의글 작성</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/siteinquiryForm.css" />
</head>
<body>
    <div class="form-container">
        <h2>문의글 작성</h2>
        <form method="post" action="write">
            <input type="text" name="title" placeholder="제목" maxlength="200" required />
            <textarea name="content" placeholder="문의 내용을 작성해주세요" required></textarea>
            <div class="form-actions">
                <a href="list">취소</a>
                <button type="submit">제출</button>
            </div>
        </form>
    </div>
</body>
</html>

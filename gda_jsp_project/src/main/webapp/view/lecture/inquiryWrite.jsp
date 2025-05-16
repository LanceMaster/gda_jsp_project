<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <title>문의글 작성</title>
  <link rel="stylesheet" href="<c:url value='/static/css/inquiry.css' />">
<div class="inquiry-write-container">
  <h2>📨 문의글 작성</h2>

  <form method="post" action="<c:url value='/lecture/inquiry/write' />">
    <input type="hidden" name="lectureId" value="1001" /> <!-- 실제 사용 시 lectureId 동적 설정 필요 -->

    <label for="title">제목:</label><br>
    <input type="text" name="title" id="title" required><br><br>

    <label for="content">내용:</label><br>
    <textarea name="content" id="content" rows="7" cols="60" required></textarea><br><br>

    <button type="submit">작성 완료</button>
    <a href="<c:url value='/lecture/inquiry/list' />">목록으로</a>
  </form>
</div>

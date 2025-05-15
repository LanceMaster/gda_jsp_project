<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageCss" value="/static/css/projectsWriteForm.css" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로젝트 글쓰기</title>
</head>
<body>
<!-- ✅ 로그인 안 했으면 로그인 페이지로 이동 -->
<c:if test="${empty sessionScope.user}">
    <script>
        location.href = '${pageContext.request.contextPath}/user/loginform';
    </script>
</c:if>
<!-- ✅ 여기까지 -->

<div class="form-container">
    <h2>팀프로젝트 글쓰기</h2>
        <form method="post" action="${pageContext.request.contextPath}/projects/write" id="projectsForm">
        <!-- 제목 입력 -->
        <input type="text" name="title" placeholder="콘텐츠 제목" required />

        <!-- 내용 입력 (Summernote) -->
        <textarea name="description" id="summernote" required></textarea>

        <!-- 썸네일 파일 업로드 -->
        <div class="file-section">
            <label>썸네일 업로드</label>
            <input type="file" name="thumbnail" accept="image/*" />
        </div>

        <!-- 태그 선택 + 추가 버튼 -->
        <h3>태그 목록 (테스트)</h3>

        <div class="tag-section">
            <label>태그 선택</label>
            <select id="tagSelect">
                <c:forEach var="tag" items="${tagList}">
                    <option value="${tag.tagId}">${tag.name}</option>
                </c:forEach>
            </select>
            <button type="button" onclick="addSelectedTag()">+ 태그추가</button>
            <div id="tagContainer"></div>
        </div>

        <!-- 동적으로 생성될 태그 전송용 hidden input -->
        <div id="hiddenTags"></div>

        <!-- 제출 / 취소 버튼 -->
        <div class="form-actions">
            <button type="button" onclick="history.back()">취소</button>
            <button type="submit">제출하기</button>
        </div>
    </form>
</div>

<!-- Summernote 로드 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
<script>
$(document).ready(function() {
    $('#summernote').summernote({
        height: 300
    });
});

// 태그 하나씩 추가
function addSelectedTag() {
    const select = document.getElementById("tagSelect");
    const selectedOption = select.options[select.selectedIndex];
    const tagContainer = document.getElementById("tagContainer");
    const hiddenTags = document.getElementById("hiddenTags");

    const tagId = selectedOption.value;
    const tagName = selectedOption.text;

    // 중복 추가 방지
    if (document.getElementById("tagHidden_" + tagId)) {
        alert("이미 추가된 태그입니다.");
        return;
    }

    // 시각적 표시
    const span = document.createElement("span");
    span.textContent = tagName;
    tagContainer.appendChild(span);

    // 전송용 hidden input
    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "tags";
    hiddenInput.value = tagId;
    hiddenInput.id = "tagHidden_" + tagId;
    hiddenTags.appendChild(hiddenInput);
}
</script>
</body>
</html>

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
<!-- ✅ 여기까지 -->

<div class="form-container">
    <h2>팀프로젝트 글쓰기</h2>
        <form method="post" action="${pageContext.request.contextPath}/projects/write" id="projectsForm">
        <!-- 제목 입력 -->
        <input type="text" name="title" placeholder="콘텐츠 제목" required />

        <!-- 내용 입력 (Summernote) -->
        <textarea name="description" id="summernote" required></textarea>


        <!-- 태그 선택 + 추가 버튼 -->

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

//태그 추가 + 삭제 기능 통합
function addSelectedTag() {
    const select = document.getElementById("tagSelect");
    const selectedOption = select.options[select.selectedIndex];
    const tagContainer = document.getElementById("tagContainer");
    const hiddenTags = document.getElementById("hiddenTags");

    const tagId = selectedOption.value;
    const tagName = selectedOption.text;

    // 중복 방지
    if (document.getElementById("tagHidden_" + tagId)) {
        alert("이미 추가된 태그입니다.");
        return;
    }

    // 시각적 표시 + 삭제 버튼
    const span = document.createElement("span");
    span.textContent = tagName + " ";
    span.id = "tagSpan_" + tagId;
    span.style.display = "inline-block";
    span.style.backgroundColor = "#e0e0e0";
    span.style.color = "#333";
    span.style.padding = "4px 8px";
    span.style.margin = "4px";
    span.style.borderRadius = "8px";

    const deleteBtn = document.createElement("button");
    deleteBtn.type = "button";
    deleteBtn.textContent = "x";
    deleteBtn.style.marginLeft = "4px";
    deleteBtn.style.background = "none";
    deleteBtn.style.border = "none";
    deleteBtn.style.color = "#ff4d4d";
    deleteBtn.style.cursor = "pointer";
    deleteBtn.onclick = function() {
        tagContainer.removeChild(span);
        hiddenTags.removeChild(document.getElementById("tagHidden_" + tagId));
    };

    span.appendChild(deleteBtn);
    tagContainer.appendChild(span);

    // 숨겨진 input
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

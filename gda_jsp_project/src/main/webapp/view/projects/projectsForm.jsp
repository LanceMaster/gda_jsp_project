<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageCss" value="/static/css/projectsForm.css" />

<!-- ✅ 누락된 link 태그 추가 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}${pageCss}">
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.css" rel="stylesheet">



<meta charset="UTF-8">
<title>프로젝트 글쓰기</title>

<div class="form-container">
    <h2>팀프로젝트 글쓰기</h2>
    <form method="post" action="${pageContext.request.contextPath}/projects/write" id="projectsForm">
        <!-- 제목 입력 -->
        <input type="text" name="title" placeholder="콘텐츠 제목" required />

        <!-- 내용 입력 (Summernote 적용) -->
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
        <div id="hiddenTags"></div>

        <!-- 제출 / 취소 버튼 -->
        <div class="form-actions">
            <button type="button" onclick="history.back()">취소</button>
            <button type="submit">제출하기</button>
        </div>
    </form>
</div>

<!-- ✅ Summernote JS 추가 (jQuery는 레이아웃에서 이미 로드됨) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.js"></script>

<script>
$(document).ready(function() {
    $('#summernote').summernote({
        height: 300,
        callbacks: {
            onImageUpload: function(files) {
                uploadImage(files[0]);
            }
        }
    });
   
    function uploadImage(file) {
        let formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "${pageContext.request.contextPath}/projects/uploadImage",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            dataType: "text", // ✅ 반환 타입 명확히 지정
            success: function(imageUrl) {
                console.log("업로드 성공, 이미지 URL:", imageUrl); // ✅ 콘솔 출력
                $('#summernote').summernote('insertImage', imageUrl);
            },
            error: function(xhr, status, error) {
                console.error("업로드 실패", xhr.responseText); // ✅ 실패 원인 출력
                alert("이미지 업로드 실패: " + xhr.responseText); // ✅ 실패 상세 표시
            }
        });
    }
});

// 태그 추가 + 삭제 기능
function addSelectedTag() {
    const select = document.getElementById("tagSelect");
    const selectedOption = select.options[select.selectedIndex];
    const tagContainer = document.getElementById("tagContainer");
    const hiddenTags = document.getElementById("hiddenTags");

    const tagId = selectedOption.value;
    const tagName = selectedOption.text;

    if (document.getElementById("tagHidden_" + tagId)) {
        alert("이미 추가된 태그입니다.");
        return;
    }

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

    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "tags";
    hiddenInput.value = tagId;
    hiddenInput.id = "tagHidden_" + tagId;
    hiddenTags.appendChild(hiddenInput);
}
</script>

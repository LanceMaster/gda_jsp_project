<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>강의 업로드</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecture_upload.css' />" />
  <script>
    let contentIndex = 0;
    function addContentField() {
      const container = document.getElementById("contentContainer");
      const block = document.createElement("div");
      block.innerHTML = `
        <label>영상 제목</label>
        <input type="text" name="contentTitles" required />
        <label>비디오 파일</label>
        <input type="file" name="contentFiles" accept="video/*" required />
        <label>재생 시간(초)</label>
        <input type="number" name="durations" min="1" required />
        <label>순서</label>
        <input type="number" name="orderNos" min="1" required />
        <hr/>
      `;
      container.appendChild(block);
      contentIndex++;
    }
  </script>
</head>
<body>
<div class="upload-container">
  <h2>강의 업로드</h2>
  <form method="post" action="${pageContext.request.contextPath}/lecture/uploadSubmit" enctype="multipart/form-data">
    <input type="text" name="lectureTitle" placeholder="강의 제목" required />
    <textarea name="lectureDescription" placeholder="강의 설명" required></textarea>
    <textarea name="curriculum" placeholder="커리큘럼 HTML" required></textarea>
    <input type="number" name="price" placeholder="가격(₩)" min="0" required />

    <label>카테고리</label>
    <select name="category" required>
      <option value="">선택</option>
      <option value="Java">Java</option>
      <option value="Spring Boot">Spring Boot</option>
      <option value="Python">Python</option>
    </select>

    <label>태그</label>
    <select id="tagSelect">
      <c:forEach var="tag" items="${tagList}">
        <option value="${tag.tagId}">${tag.name}</option>
      </c:forEach>
    </select>
    <button type="button" onclick="addSelectedTag()">+ 태그 추가</button>
    <div id="tagContainer"></div>
    <div id="hiddenTags"></div>

    <label>썸네일 이미지</label>
    <input type="file" name="thumbnailFile" accept="image/*" required />

    <h3>📽️ 강의 콘텐츠 추가</h3>
    <div id="contentContainer"></div>
    <button type="button" onclick="addContentField()">+ 콘텐츠 추가</button>

    <br><br>
    <button type="submit">강의 등록</button>
  </form>
</div>

<script>
  const selectedTagIds = new Set();
  function addSelectedTag() {
    const select = document.getElementById("tagSelect");
    const tagId = select.value;
    const tagName = select.options[select.selectedIndex].text;
    if (selectedTagIds.has(tagId)) {
      alert("이미 추가된 태그입니다."); return;
    }
    selectedTagIds.add(tagId);
    const label = document.createElement("span");
    label.className = "tag-label";
    label.textContent = tagName;
    const delBtn = document.createElement("button");
    delBtn.type = "button"; delBtn.textContent = "x";
    delBtn.onclick = () => {
      label.remove(); document.getElementById("tagHidden_" + tagId).remove();
      selectedTagIds.delete(tagId);
    };
    label.appendChild(delBtn);
    document.getElementById("tagContainer").appendChild(label);
    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden"; hiddenInput.name = "tags";
    hiddenInput.id = "tagHidden_" + tagId; hiddenInput.value = tagId;
    document.getElementById("hiddenTags").appendChild(hiddenInput);
  }
</script>
</body>
</html>

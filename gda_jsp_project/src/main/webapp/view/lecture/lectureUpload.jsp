<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>강의 콘텐츠 업로드</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecture_upload.css' />" />
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

    <label>강의 비디오</label>
    <input type="file" name="contentFile" accept="video/*" required />

    <input type="number" name="duration" placeholder="재생시간(초)" required min="1" />
    <input type="number" name="orderNo" placeholder="순서" required min="1" />
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
      alert("이미 추가된 태그입니다.");
      return;
    }

    selectedTagIds.add(tagId);

    const tagLabel = document.createElement("span");
    tagLabel.className = "tag-label";
    tagLabel.textContent = tagName;

    const deleteBtn = document.createElement("button");
    deleteBtn.type = "button";
    deleteBtn.textContent = "x";
    deleteBtn.onclick = () => {
      tagLabel.remove();
      document.getElementById("tagHidden_" + tagId).remove();
      selectedTagIds.delete(tagId);
    };

    tagLabel.appendChild(deleteBtn);
    document.getElementById("tagContainer").appendChild(tagLabel);

    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "tags";
    hiddenInput.id = "tagHidden_" + tagId;
    hiddenInput.value = tagId;
    document.getElementById("hiddenTags").appendChild(hiddenInput);
  }
</script>
</body>
</html>

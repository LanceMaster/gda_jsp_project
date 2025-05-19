<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>강의 업로드</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lectureUpload.css' />" />
</head>
<body>
  <div class="upload-container">
    <h2>강의 업로드</h2>
    <form method="post" action="${pageContext.request.contextPath}/lecture/uploadSubmit" enctype="multipart/form-data">

      <label>강의 제목</label>
      <input type="text" name="lectureTitle" placeholder="강의 제목" required />

      <label>강의 설명</label>
      <textarea name="lectureDescription" placeholder="강의 설명" required></textarea>

      <label>커리큘럼 (HTML)</label>
      <textarea name="curriculum" placeholder="커리큘럼 HTML" required></textarea>

      <label>가격 (₩)</label>
      <input type="number" name="price" placeholder="가격(₩)" min="0" required />

      <label>카테고리</label>
      <select name="category" required>
        <option value="">선택</option>
        <option value="Spring Boot">Spring Boot</option>
        <option value="CSS">CSS</option>
        <option value="Python">Python</option>
        <option value="백엔드">백엔드</option>
        <option value="정보보안">정보보안</option>
      </select>

      <label>썸네일 이미지</label>
      <div class="drop-zone" id="thumbDropZone">
        <span>이미지를 여기에 드래그하거나 클릭하여 업로드</span>
        <input type="file" name="thumbnailFile" accept="image/*" required hidden />
      </div>

      <hr/>

      <h3>📽️ 콘텐츠 정보 입력</h3>
      <div id="contentMetaContainer"></div>
      <button type="button" onclick="addContentMeta()">+ 콘텐츠 메타정보 추가</button>

      <br><br>

      <h3>🎞️ 영상 업로드 (다중 선택 가능)</h3>
      <label class="file-upload-btn">
        비디오 파일 선택
        <input type="file" name="contentFiles" accept="video/*" multiple class="file-upload-input" required />
      </label>

      <br><br>
      <button type="submit">강의 등록</button>
    </form>
  </div>

  <script>
    // 썸네일 드래그앤드롭
    function setupDropZone(dropZoneId) {
      const dropZone = document.getElementById(dropZoneId);
      const fileInput = dropZone.querySelector('input[type="file"]');

      dropZone.addEventListener("click", () => fileInput.click());
      dropZone.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropZone.classList.add("drop-zone--active");
      });
      dropZone.addEventListener("dragleave", () => {
        dropZone.classList.remove("drop-zone--active");
      });
      dropZone.addEventListener("drop", (e) => {
        e.preventDefault();
        dropZone.classList.remove("drop-zone--active");
        const files = e.dataTransfer.files;
        if (files.length > 0) {
          fileInput.files = files;
          dropZone.querySelector("span").textContent = files[0].name;
        }
      });
      fileInput.addEventListener("change", () => {
        if (fileInput.files.length > 0) {
          dropZone.querySelector("span").textContent = fileInput.files[0].name;
        }
      });
    }

    // 콘텐츠 메타정보 (제목, 순서) 입력 필드
    function addContentMeta() {
      const container = document.getElementById("contentMetaContainer");
      const div = document.createElement("div");
      div.classList.add("content-block");
      div.innerHTML = `
        <label>영상 제목</label>
        <input type="text" name="contentTitles" required />

        <label>순서</label>
        <input type="number" name="orderNos" min="1" required />
        <hr/>
      `;
      container.appendChild(div);
    }

    window.onload = function () {
      setupDropZone("thumbDropZone");
      addContentMeta(); // 기본 메타 1개
    };
  </script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
  <meta charset="UTF-8">
  <title>강의 콘텐츠 업로드</title>
<link rel="stylesheet" href="<c:url value='/static/css/lecture.css' />" />

<div class="container">
  <h2>콘텐츠 업로드</h2>

  <form method="post" action="/lecture/lectureUpload" enctype="multipart/form-data">

    <!-- 강의 제목 -->
    <input type="text" name="lectureTitle" placeholder="콘텐츠 제목" required />

    <!-- 설명 -->
    <label for="lectureDescription">설명</label>
    <textarea name="lectureDescription" placeholder="내용을 입력하세요" required></textarea>

    <!-- 태그 -->
    <div class="tag-area">
      <input type="text" name="tag" placeholder="태그 추가하기" />
      <button type="button">태그 추가하기</button>
    </div>

    <!-- 🎬 강의 동영상 업로드 -->
    <label>강의 동영상 File</label>
    <div class="file-drop" id="video-drop">📁 파일을 이곳에 드래그 하거나 놓으세요</div>
    <div class="upload-row">
      <input type="file" id="videoInput" name="contentFile" accept="video/*,.pdf,.ppt,.pptx" required />
    </div>

    <!-- 🖼️ 썸네일 이미지 업로드 -->
    <label>썸네일 이미지 File</label>
    <div class="file-drop" id="thumbnail-drop">📁 파일을 이곳에 드래그 하거나 놓으세요</div>
    <div class="upload-row">
      <input type="file" id="thumbnailInput" name="thumbnailFile" accept="image/*" required />
    </div>

    <!-- 💵 판매가격 -->
    <input type="number" name="price" placeholder="판매가격" min="0" required />

    <!-- 등록 버튼 -->
    <button type="submit">게시하기</button>
  </form>
</div>

<!-- ✅ 드래그 앤 드롭 스크립트 -->
<script>
  function bindFileDrop(dropZoneId, inputId) {
    const dropZone = document.getElementById(dropZoneId);
    const fileInput = document.getElementById(inputId);

    dropZone.addEventListener("dragover", function(e) {
      e.preventDefault();
      dropZone.classList.add("dragover");
    });

    dropZone.addEventListener("dragleave", function(e) {
      e.preventDefault();
      dropZone.classList.remove("dragover");
    });

    dropZone.addEventListener("drop", function(e) {
      e.preventDefault();
      dropZone.classList.remove("dragover");

      const files = e.dataTransfer.files;
      if (files.length > 0) {
        fileInput.files = files;
      }
    });
  }

  // 📦 각각 드롭 구역에 기능 바인딩
  bindFileDrop("thumbnail-drop", "thumbnailInput");
  bindFileDrop("video-drop", "videoInput");
</script>

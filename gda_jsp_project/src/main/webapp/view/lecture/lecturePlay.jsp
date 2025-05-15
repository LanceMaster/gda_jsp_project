<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.net.URLEncoder" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>${lecture.title}</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css'/>" />
</head>
<body>
<div class="lecture-container">

  <!-- ✅ 강의 제목 -->
  <h1 class="lecture-title">${lecture.title}</h1>

  <!-- ✅ 강의 콘텐츠 (스트리밍 영상) -->
  <div class="video-wrapper">
    <video id="lecture-video" controls crossorigin="anonymous" poster="${lecture.thumbnail}">
      <source src="${content.url}" type="application/x-mpegURL" />
      <!-- HLS 형식 스트리밍 (예: m3u8), DASH 사용 시 타입 변경 필요 -->
      자바스크립트를 활성화해야 강의를 볼 수 있습니다.
    </video>
  </div>

  <!-- ✅ 재생 컨트롤 하단 바 -->
  <div class="video-controls">
    <span class="time-display">
      🕒 <span id="current-time">00:00</span>
    </span>
    <input type="range" id="seek-bar" value="0" />
  </div>

  <!-- ✅ 태그 리스트 -->
  <div class="tags">
    <c:forEach var="tag" items="${tags}">
      <span class="tag">${tag.name}</span>
    </c:forEach>
  </div>

  <!-- ✅ 강의 설명 -->
  <div class="lecture-description">
    <h3>📌 기획의도: 왜 이 조합인가?</h3>
    <p><c:out value="${lecture.description}" /></p>
  </div>

</div>

<script>
  const video = document.getElementById("lecture-video");
  const seekBar = document.getElementById("seek-bar");
  const contentId = "${content.contentId}";  // 콘텐츠 PK
  const userId = "${sessionScope.loginUser.userId}"; // 세션 로그인 유저 PK

  // ⏱ 10초 간격 저장
  setInterval(() => {
    const percent = Math.floor((video.currentTime / video.duration) * 100);
    if (percent >= 0 && video.duration > 0) {
      fetch("<c:url value='/lecture/saveProgress' />", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `contentId=${contentId}&userId=${userId}&progress=${percent}`
      });
    }
  }, 10000);
</script>


<!-- ✅ 자바스크립트: 재생 시간 표시 -->
<script>
  const video = document.getElementById("lecture-video");
  const currentTimeDisplay = document.getElementById("current-time");
  const seekBar = document.getElementById("seek-bar");

  video.addEventListener("timeupdate", () => {
    const minutes = Math.floor(video.currentTime / 60).toString().padStart(2, '0');
    const seconds = Math.floor(video.currentTime % 60).toString().padStart(2, '0');
    currentTimeDisplay.textContent = `${minutes}:${seconds}`;
    seekBar.value = video.currentTime;
  });

  video.addEventListener("loadedmetadata", () => {
    seekBar.max = video.duration;
  });

  seekBar.addEventListener("input", () => {
    video.currentTime = seekBar.value;
  });
</script>

</body>
</html>

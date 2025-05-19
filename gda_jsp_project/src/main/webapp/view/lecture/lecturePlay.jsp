<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.net.URLEncoder" %>

<%-- ✅ URL 변수 미리 정의 --%>
<c:url var="progressUrl" value="/lecture/progress/update" />
<c:url var="loginUrl" value="/user/login" />

  <meta charset="UTF-8" />
  <title>${lecture.title}</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css' />" />
  <script src="<c:url value='/static/js/lecturePlay.js' />" defer></script> <%-- ✅ JS 외부 로딩 --%>

<div class="lecture-container">

  <!-- ✅ 강의 제목 -->
  <h1 class="lecture-title">${lecture.title}</h1>

  <!-- ✅ 강의 콘텐츠 (스트리밍 영상) -->
  <div class="video-wrapper">
    <video id="lecture-video" controls crossorigin="anonymous"
           data-content-id="${content.contentId}"
           data-user-id="${sessionScope.loginUser.userId}"
           data-lecture-id="${lecture.lectureId}"
           data-progress-url="${progressUrl}"
           <c:if test="${not empty lecture.thumbnail}">
             poster="${lecture.thumbnail}"
           </c:if>>
      <source src="${content.url}" type="application/x-mpegURL" />
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

<!-- ✅ 콘텐츠 리스트 (좌측 사이드바) -->
<div class="content-list">
  <ul>
    <c:forEach var="item" items="${contentList}">
      <li>
        <a href="${pageContext.request.contextPath}/lecture/play?lectureId=${lecture.lectureId}&contentId=${item.contentId}">
          ${item.title} (${item.duration}초)
        </a>
      </li>
    </c:forEach>
  </ul>
</div>


<!-- 
<c:if test="${empty sessionScope.loginUser}">
  <script>
    alert('로그인 후 시청 가능합니다.');
    location.href = '${loginUrl}';
  </script>
</c:if>

<script>
  const video = document.getElementById("lecture-video");
  const seekBar = document.getElementById("seek-bar");
  const currentTimeDisplay = document.getElementById("current-time");

  const contentId = "${content.contentId}";
  const userId = "${sessionScope.loginUser.userId}";
  const progressUrl = "${progressUrl}";

  // 10초마다 진도 저장
  setInterval(() => {
    if (video.duration > 0) {
      const percent = Math.floor((video.currentTime / video.duration) * 100);
      if (percent >= 0) {
        fetch(progressUrl, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: `contentId=${contentId}&userId=${userId}&progress=${percent}&lectureId=${lecture.lectureId}`
        });
      }
    }
  }, 10000);

  // 재생 위치 표시 + 슬라이더 바 연동
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
  
 
//콘텐츠 시청 완료 시 AJAX 요청
  fetch("/lecture/progress/update", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: `lectureId=1001&contentId=2001&progress=100`
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
      console.log("진도 저장 및 수료 확인 완료");
    }
  });
</script>
-->

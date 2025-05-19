<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>${lecture.title} - 강의 시청</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css'/>" />
  <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
  <style>
    body {
      display: flex;
      margin: 0;
      font-family: 'Segoe UI', sans-serif;
    }
    .video-section {
      flex: 3;
      padding: 40px;
      position: relative;
    }
    .sidebar {
      flex: 1;
      background: #f9f9f9;
      border-left: 1px solid #ddd;
      height: 100vh;
      overflow-y: auto;
      padding: 20px;
      box-shadow: -2px 0 6px rgba(0,0,0,0.05);
    }
    .sidebar h4 {
      margin-bottom: 20px;
      color: #333;
    }
    .sidebar ul {
      list-style: none;
      padding: 0;
    }
    .sidebar li {
      margin-bottom: 12px;
      cursor: pointer;
      padding: 8px 12px;
      border-radius: 8px;
    }
    .sidebar li:hover, .sidebar li.active {
      background: #e0e7ff;
      color: #1d4ed8;
      font-weight: bold;
    }
    .completed-badge {
      font-size: 0.8rem;
      color: green;
      margin-left: 8px;
    }
    #loadingMessage {
      position: absolute;
      top: 48%;
      left: 48%;
      background: rgba(255,255,255,0.95);
      padding: 20px;
      border-radius: 10px;
      font-size: 16px;
      color: #333;
      box-shadow: 0 0 8px rgba(0,0,0,0.1);
      display: none;
    }
    .lecture-title {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 12px;
    }
    .lecture-instructor {
      font-size: 16px;
      color: #777;
      margin-bottom: 20px;
    }
    .lecture-description {
      margin-bottom: 24px;
      font-size: 15px;
      line-height: 1.6;
    }
  </style>
</head>
<body>

  <!-- ✅ 메인 영상 영역 -->
  <div class="video-section">
    <div class="lecture-title">${lecture.title}</div>
    <div class="lecture-instructor">강사: ${instructor.name}</div>
    <p class="lecture-description">${lecture.description}</p>

    <div id="loadingMessage">⏳ 영상 로딩 중입니다...</div>

    <video id="lectureVideo" width="960" height="540" controls></video>
    <div id="currentContentTitle" style="margin-top:12px; font-weight: 500;"></div>
  </div>

  <!-- ✅ 콘텐츠 사이드바 -->
  <div class="sidebar">
    <h4>📑 콘텐츠 바로가기</h4>
    <ul id="contentList">
      <c:forEach var="content" items="${contents}" varStatus="status">
        <li onclick="playContent(${status.index})" id="content-${content.contentId}">
          [${content.orderNo}] ${content.title} (${content.duration}초)
          <span class="completed-badge" id="badge-${content.contentId}" style="display:none;">✅ 완료</span>
        </li>
      </c:forEach>
    </ul>
  </div>

  <!-- ✅ 스크립트 -->
  <script>
    const contents = [
      <c:forEach var="c" items="${contents}" varStatus="loop">
        {
          contentId: ${c.contentId},
          title: '${c.title}',
          url: '${c.url}',  // contextPath는 이미 포함되어 있음 (/upload/hls/...)
          duration: ${c.duration}
        }<c:if test="${!loop.last}">,</c:if>
      </c:forEach>
    ];

    const video = document.getElementById("lectureVideo");
    const loadingMessage = document.getElementById("loadingMessage");
    const titleDisplay = document.getElementById("currentContentTitle");

    let currentIndex = 0;
    let lastSentPercent = 0;
    let hls = null;

    function playContent(index) {
      currentIndex = index;
      const content = contents[index];
      titleDisplay.innerText = `📺 현재 콘텐츠: ${content.title}`;
      loadingMessage.style.display = 'block';

      // HLS 지원 브라우저
      if (Hls.isSupported()) {
        if (hls) hls.destroy();
        hls = new Hls();
        hls.loadSource(content.url);
        hls.attachMedia(video);
        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          video.play();
          loadingMessage.style.display = 'none';
        });
      } 
      // Safari 전용 fallback
      else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = content.url;
        video.addEventListener('loadedmetadata', () => {
          video.play();
          loadingMessage.style.display = 'none';
        });
      } else {
        alert("⚠️ 현재 브라우저에서는 HLS 스트리밍을 지원하지 않습니다.");
      }

      updateActiveUI(content.contentId);
    }

    function updateActiveUI(contentId) {
      document.querySelectorAll("#contentList li").forEach(li => li.classList.remove("active"));
      const activeItem = document.getElementById("content-" + contentId);
      if (activeItem) activeItem.classList.add("active");
    }

    video.addEventListener("timeupdate", function () {
      const percent = Math.round((video.currentTime / video.duration) * 100);
      if (Math.abs(percent - lastSentPercent) >= 10 && percent <= 100) {
        lastSentPercent = percent;

        fetch("${pageContext.request.contextPath}/lecture/progress/update", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            lectureId: "${lecture.lectureId}",
            contentId: contents[currentIndex].contentId,
            progress: percent
          })
        })
        .then(res => res.json())
        .then(res => {
          if (res.success && percent === 100) {
            document.getElementById("badge-" + contents[currentIndex].contentId).style.display = 'inline';
          }
        })
        .catch(err => {
          console.error("진도율 전송 실패:", err);
        });
      }
    });

    video.addEventListener("ended", function () {
      if (currentIndex < contents.length - 1) {
        playContent(currentIndex + 1);
      }
    });

    window.onload = function () {
      playContent(0);
    };
  </script>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>${lecture.title} - 강의 시청</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css'/>" />
  <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
  <style>
    body {
      margin: 0;
      display: flex;
      font-family: 'Apple SD Gothic Neo', 'Segoe UI', sans-serif;
      background-color: #f9fafb;
      color: #111;
    }
    .video-section {
      flex: 3;
      min-width: 720px;
      padding: 40px;
      background: white;
    }
    .sidebar {
      flex: 1;
      min-width: 300px;
      background: #f1f3f5;
      border-left: 1px solid #e5e8eb;
      height: 100vh;
      overflow-y: auto;
      padding: 32px 24px;
    }
    .lecture-title {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 12px;
    }
    .lecture-instructor {
      font-size: 16px;
      color: #6b7280;
      margin-bottom: 20px;
    }
    .lecture-description {
      font-size: 15px;
      color: #374151;
      margin-bottom: 32px;
      line-height: 1.6;
    }
    #loadingMessage {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background: white;
      border-radius: 12px;
      padding: 20px 30px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      font-size: 16px;
      display: none;
      z-index: 1000;
    }
    video {
      width: 100%;
      max-width: 960px;
      border-radius: 12px;
      background: black;
      border: 1px solid #d1d5db;
    }
    .sidebar h4 {
      font-size: 18px;
      font-weight: 500;
      margin-bottom: 24px;
    }
    .sidebar ul {
      list-style: none;
      padding: 0;
    }
    .sidebar li {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      background: white;
      border-radius: 12px;
      margin-bottom: 12px;
      cursor: pointer;
      transition: background 0.2s;
    }
    .sidebar li:hover {
      background: #e0f2fe;
    }
    .sidebar li.active {
      background: #dbeafe;
      border-left: 4px solid #2563eb;
    }
    .sidebar li.completed {
      color: #10b981;
      font-weight: 600;
    }
    .progress-percent {
      font-size: 13px;
      color: #6b7280;
      margin-left: 8px;
      white-space: nowrap;
    }
    #currentContentTitle {
      font-size: 16px;
      margin-top: 16px;
    }
  </style>
</head>
<body>

<div class="video-section">
  <div class="lecture-title">${lecture.title}</div>
  <div class="lecture-instructor">강사: ${instructor.name}</div>
  <div class="lecture-description">${lecture.description}</div>

  <div id="loadingMessage">⏳ 콘텐츠 로딩 중입니다...</div>

  <video id="lectureVideo" controls></video>
  <div id="currentContentTitle">📺 콘텐츠를 준비 중입니다...</div>
</div>

<div class="sidebar">
  <h4>📑 콘텐츠 목록</h4>
  <ul id="contentList">
    <c:forEach var="content" items="${contents}" varStatus="status">
      <li id="content-${content.contentId}" tabindex="0"
          onclick="playContent(${status.index})"
          onkeydown="if(event.key==='Enter') playContent(${status.index})">
        <span>[${content.orderNo}] ${content.title}</span>
        <span class="progress-percent" id="progress-${content.contentId}">0%</span>
      </li>
    </c:forEach>
  </ul>
</div>

<script>
  const contents = [
    <c:forEach var="c" items="${contents}" varStatus="loop">
      {
        contentId: ${c.contentId},
        title: '${c.title}',
        url: '${c.url}',
        duration: ${c.duration},
        progress: 0
      }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];

  const video = document.getElementById("lectureVideo");
  const loadingMessage = document.getElementById("loadingMessage");
  const titleDisplay = document.getElementById("currentContentTitle");

  let currentIndex = 0;
  let lastSentPercent = 0;
  let lastReportedTime = 0;
  let hls = null;

  function playContent(index) {
    currentIndex = index;
    const content = contents[index];
    titleDisplay.innerText = "📺 현재 콘텐츠: " + content.title;
    loadingMessage.style.display = 'block';

    if (Hls.isSupported()) {
      if (hls) hls.destroy();
      hls = new Hls();
      hls.loadSource(content.url);
      hls.attachMedia(video);
      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        video.play();
        loadingMessage.style.display = 'none';
      });
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
      video.src = content.url;
      video.addEventListener('loadedmetadata', () => {
        video.play();
        loadingMessage.style.display = 'none';
      });
    } else {
      alert("⚠️ 브라우저에서 HLS를 지원하지 않습니다.");
      loadingMessage.style.display = 'none';
    }

    updateActiveUI(content.contentId);
    lastSentPercent = 0;
    lastReportedTime = 0;
  }

  function updateActiveUI(contentId) {
    document.querySelectorAll("#contentList li").forEach(li => li.classList.remove("active"));
    const li = document.getElementById("content-" + contentId);
    if (li) li.classList.add("active");
  }

  video.addEventListener("timeupdate", () => {
    const currentTime = Math.floor(video.currentTime);
    const percent = Math.round((video.currentTime / video.duration) * 100);

    if (currentTime - lastReportedTime >= 5 && percent <= 100) {
      lastReportedTime = currentTime;
      lastSentPercent = percent;
      const content = contents[currentIndex];

      fetch("${pageContext.request.contextPath}/lecture/progress/update", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          lectureId: "${lecture.lectureId}",
          contentId: content.contentId,
          progress: percent
        })
      })
      .then(res => res.json())
      .then(res => {
        if (res.success) {
          contents[currentIndex].progress = percent;
          document.getElementById("progress-" + content.contentId).innerText = percent + "%";

          const li = document.getElementById("content-" + content.contentId);
          if (percent === 100 && li) {
            li.classList.add("completed");
          }
        }
      })
      .catch(err => {
        console.error("진도율 전송 실패:", err);
      });
    }
  });

  video.addEventListener("ended", () => {
    if (currentIndex < contents.length - 1) {
      playContent(currentIndex + 1);
    }
  });

  window.onload = () => {
    playContent(0);
  };
</script>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>${lecture.title} - 강의 시청</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css'/>" />
  <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
<style>
/* ✅ 기본 Reset + 토스 스타일 전반 적용 */
body {
  margin: 0;
  display: flex;
  font-family: 'Spoqa Han Sans Neo', 'Apple SD Gothic Neo', 'Segoe UI', sans-serif;
  background-color: #fefefe;
  color: #1c1c1e;
}

.video-section {
  flex: 3;
  min-width: 720px;
  padding: 48px 64px;
  background: #ffffff;
}

.sidebar {
  flex: 1;
  min-width: 300px;
  background: #f9fafb;
  border-left: 1px solid #e5e7eb;
  height: 100vh;
  overflow-y: auto;
  padding: 32px 24px;
  box-shadow: inset 4px 0 8px rgba(0, 0, 0, 0.02);
}

/* 🎯 타이틀 & 설명 */
.lecture-title {
  font-size: 24px;
  font-weight: 600;
  color: #111827;
  margin-bottom: 12px;
}

.lecture-instructor {
  font-size: 15px;
  color: #6b7280;
  margin-bottom: 16px;
}

.lecture-description {
  font-size: 15px;
  color: #374151;
  line-height: 1.6;
  margin-bottom: 32px;
}

/* 🎥 비디오 */
video {
  width: 100%;
  max-width: 960px;
  border-radius: 12px;
  background: #000;
  border: none;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.3s ease;
}
video:hover {
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

/* 🌀 로딩 메시지 */
#loadingMessage {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #ffffff;
  color: #1f2937;
  font-weight: 500;
  border: 1px solid #d1d5db;
  padding: 16px 24px;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
  font-size: 15px;
  display: none;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

/* ✅ 현재 콘텐츠 정보 */
#currentContentTitle {
  font-size: 15px;
  margin-top: 18px;
  color: #3f3f46;
  font-weight: 500;
  background: #f3f4f6;
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

/* 📑 사이드바 리스트 */
.sidebar h4 {
  font-size: 17px;
  font-weight: 600;
  margin-bottom: 18px;
  color: #111827;
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
  background: #ffffff;
  border-radius: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  font-size: 15px;
  box-shadow: inset 0 0 0 1px #e0e0e0;
  transition: all 0.2s ease;
}

.sidebar li:hover {
  background: #f0f4ff;
  transform: translateX(2px);
  box-shadow: inset 0 0 0 2px #4f46e5;
}

.sidebar li.active {
  background: #eef2ff;
  border-left: 4px solid #6366f1;
  font-weight: 600;
}

.sidebar li.completed {
  background: #ecfdf5;
  color: #059669;
  border-left: 4px solid #10b981;
}

.sidebar li:focus {
  outline: none;
  box-shadow: 0 0 0 2px #6366f1;
  background: #eef2ff;
}

/* ⏱️ 진도율 */
.progress-percent {
  font-size: 12px;
  font-weight: 500;
  color: #6b7280;
  margin-left: 6px;
  white-space: nowrap;
}

/* 📱 반응형 */
@media (max-width: 960px) {
  body {
    flex-direction: column;
  }

  .video-section {
    width: 100%;
    padding: 24px;
    min-width: unset;
  }

  .sidebar {
    order: -1;
    width: 100%;
    min-width: unset;
    border-left: none;
    border-top: 1px solid #e5e7eb;
    padding: 20px;
  }

  .sidebar li {
    font-size: 14px;
    padding: 10px 14px;
  }
  
  .sidebar li.completed::after {
  content: "✔ 수료";
  font-size: 12px;
  font-weight: 600;
  color: #059669;
  margin-left: 8px;
}

.completion-badge {
  font-size: 12px;
  font-weight: 600;
  color: #059669;
  margin-left: 8px;
}

  
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
        <span class="progress-percent" id="progress-${content.contentId}">
  ${content.progressPercent}%
</span>
      </li>
    </c:forEach>
  </ul>
</div>

<script>

window.onload = () => {
	  // ✅ 진도율 텍스트를 초기 렌더링에 반영
	  contents.forEach(content => {
	    const el = document.getElementById("progress-" + content.contentId);
	    if (el) el.innerText = content.progress + "%";

	    // ✅ 100%인 콘텐츠는 completed 클래스도 미리 부여
	    if (content.progress === 100) {
	      const li = document.getElementById("content-" + content.contentId);
	      if (li) li.classList.add("completed");
	    }
	  });

	  playContent(0);
	};

  const contents = [
    <c:forEach var="c" items="${contents}" varStatus="loop">
      {
        contentId: ${c.contentId},
        title: '${c.title}',
        url: '${c.url}',
        duration: ${c.duration > 0 ? c.duration : 300},
        progress: ${c.progressPercent}


      }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];

  const video = document.getElementById("lectureVideo");
  const loadingMessage = document.getElementById("loadingMessage");
  const titleDisplay = document.getElementById("currentContentTitle");

  let currentIndex = 0;
  let hls = null;
  let playedTime = 0;
  let lastCheckTime = 0;
  let lastSentPercent = 0;
  let isPlaying = false;

  function isValidContent(content) {
    if (!content.url || content.url.trim() === "") {
      alert("⚠️ 콘텐츠 URL이 없습니다. 관리자에게 문의하세요.");
      return false;
    }

    if (content.duration < 3) {
      alert("⚠️ 영상 길이가 너무 짧아 재생할 수 없습니다.");
      return false;
    }

    return true;
  }

  const originalPlayContent = function(index) {
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
	      video.addEventListener("loadedmetadata", () => {
	        if (content.progress > 0 && video.duration) {
	          video.currentTime = video.duration * (content.progress / 100);
	        }

	        // ✅ 여기서 playedTime 초기화
	        playedTime = video.duration * (content.progress / 100);
	        lastSentPercent = content.progress;
	        lastCheckTime = video.currentTime;
	      });

	      video.play();
	      loadingMessage.style.display = 'none';
	    });
	  } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
	    video.src = content.url;

	    video.addEventListener("loadedmetadata", () => {
	      if (content.progress > 0 && video.duration) {
	        video.currentTime = video.duration * (content.progress / 100);
	      }

	      playedTime = video.duration * (content.progress / 100);
	      lastSentPercent = content.progress;
	      lastCheckTime = video.currentTime;

	      video.play();
	      loadingMessage.style.display = 'none';
	    });
	  } else {
	    alert("⚠️ 브라우저에서 HLS를 지원하지 않습니다.");
	    loadingMessage.style.display = 'none';
	  }

	  updateActiveUI(content.contentId);
	};


  playContent = function(index) {
    if (!contents || contents.length === 0) {
      alert("⚠️ 시청 가능한 콘텐츠가 없습니다.");
      return;
    }

    const content = contents[index];
    if (!isValidContent(content)) return;

    if (content.progress === 100) {
      const proceed = confirm("✔ 이미 완료된 콘텐츠입니다.\n다시 재생하시겠습니까?");
      if (!proceed) return;
    }

    originalPlayContent(index);
  };

  function updateActiveUI(contentId) {
    document.querySelectorAll("#contentList li").forEach(li => li.classList.remove("active"));
    const li = document.getElementById("content-" + contentId);
    if (li) li.classList.add("active");
  }

  video.addEventListener("play", () => {
    isPlaying = true;
    lastCheckTime = video.currentTime;
  });

  video.addEventListener("pause", () => {
    isPlaying = false;
  });

  video.addEventListener("seeking", () => {
    lastCheckTime = video.currentTime;
  });

  video.addEventListener("timeupdate", () => {
	  if (!isPlaying || video.seeking || video.paused) return;

	  const now = video.currentTime;
	  const delta = now - lastCheckTime;

	  if (delta > 0 && delta < 5) {
	    playedTime += delta;
	  }

	  lastCheckTime = now;

	  const content = contents[currentIndex];
	  const percent = Math.min(100, Math.round((playedTime / content.duration) * 100));

	  // ✅ 이미 100%면 더 이상 전송 안함
	  if (content.progress >= 100) return;

	  if (percent - lastSentPercent >= 5 || (percent === 100 && lastSentPercent < 100)) {
	    lastSentPercent = percent;

	    // 실시간 표시
	    const progressEl = document.getElementById("progress-" + content.contentId);
	    if (progressEl) {
	      progressEl.innerText = percent + "%";
	    }

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

	        if (percent === 100) {
	          const li = document.getElementById("content-" + content.contentId);
	          if (li) li.classList.add("completed");
	        }
	      }
	    })
	    .catch(err => {
	      console.error("❌ 진도율 전송 실패:", err);
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
  
  video.addEventListener("ended", () => {
	  const content = contents[currentIndex];
	  if (content.progress < 100) {
	    fetch("${pageContext.request.contextPath}/lecture/progress/update", {
	      method: "POST",
	      headers: { "Content-Type": "application/json" },
	      body: JSON.stringify({
	        lectureId: "${lecture.lectureId}",
	        contentId: content.contentId,
	        progress: 100
	      })
	    })
	    .then(res => res.json())
	    .then(res => {
	      if (res.success) {
	        contents[currentIndex].progress = 100;

	        const el = document.getElementById("progress-" + content.contentId);
	        if (el) el.innerText = "100%";

	        const li = document.getElementById("content-" + content.contentId);
	        if (li) li.classList.add("completed");

	        const badge = document.getElementById("badge-" + content.contentId);
	        if (badge) badge.innerText = "✔ 수료";
	      }
	    });
	  }

	  if (currentIndex < contents.length - 1) {
	    playContent(currentIndex + 1);
	  }
	});

  
</script>


</body>
</html>

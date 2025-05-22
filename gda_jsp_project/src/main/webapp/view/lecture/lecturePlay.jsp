<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>${lecture.title} - 강의 시청</title>
  <link rel="stylesheet" href="<c:url value='/static/css/lecturePlay.css'/>" />
  <script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
<style>
:root {
  --bg: #fff;
  --surface: #fff;
  --border: #e5e7eb;
  --primary: #6366f1;
  --primary-soft: #eff0fe;
  --primary-dark: #4549d6;
  --secondary: #10b981;
  --secondary-soft: #e6faf3;
  --text: #202124;
  --text-soft: #8e98a8;
  --shadow: 0 6px 32px rgba(29,41,75,0.07);
  --shadow-lg: 0 18px 52px rgba(34,43,65,0.14);
  --radius-xl: 2.2rem;
  --radius-md: 1.2rem;
}

/* 전체 배경 */
body {
  margin: 0;
  display: flex;
  min-height: 100vh;
  background: var(--bg);
  color: var(--text);
  font-family: 'Spoqa Han Sans Neo', 'Apple SD Gothic Neo', 'Pretendard', 'Segoe UI', sans-serif;
  -webkit-font-smoothing: antialiased;
  transition: background 0.3s, color 0.2s;
}

/* ===== 레이아웃 ===== */
.video-section {
  flex: 3;
  min-width: 720px;
  padding: 52px 68px 46px 68px;
  background: var(--surface);
  box-shadow: var(--shadow);
  border-radius: var(--radius-xl) 0 0 var(--radius-xl);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: background 0.3s;
}

.sidebar {
  flex: 1.1;
  min-width: 325px;
  background: var(--primary-soft);
  border-left: 2.5px solid var(--border);
  height: 100vh;
  overflow-y: auto;
  padding: 46px 30px 28px 30px;
  box-shadow: var(--shadow-lg);
  border-radius: 0 var(--radius-xl) var(--radius-xl) 0;
  display: flex;
  flex-direction: column;
  gap: 19px;
}

@media (max-width: 1080px) {
  body { flex-direction: column; }
  .video-section { width: 100%; min-width: unset; border-radius: 0 0 var(--radius-xl) var(--radius-xl); box-shadow: none;}
  .sidebar { order: -1; width: 100%; min-width: unset; border-left: none; border-top: 2.5px solid var(--border); border-radius: var(--radius-xl);}
}

/* ===== 타이틀/강사/설명 ===== */
.lecture-title {
  font-size: 2.22rem;
  font-weight: 900;
  color: var(--primary);
  letter-spacing: -1.1px;
  background: linear-gradient(90deg, #6366f1 70%, #a5b4fc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 12px;
}

.lecture-description {
  font-size: 1.11rem;
  color: var(--text-soft);
  margin-top: 36px;
  margin-bottom: 0;
  line-height: 1.77;
}

/* ===== 비디오 영역 ===== */
video {
  width: 100%;
  max-width: 960px;
  border-radius: 20px;
  background: #fff !important;   /* 비어있는 부분도 흰색 */
  border: 1.6px solid var(--border);
  box-shadow: 0 8px 40px rgba(50,60,120,0.09);
  margin-bottom: 20px;
  margin-top: 2px;
  outline: none;
  transition: box-shadow .27s, border .22s;
  object-fit: contain;
}
video:focus {
  border: 1.6px solid var(--primary);
  box-shadow: 0 4px 14px rgba(99,102,241,0.10);
}
video:hover {
  box-shadow: 0 12px 28px rgba(99,102,241,0.14);
}

/* ===== 로딩/알림 ===== */
#loadingMessage, .alert {
  position: absolute;
  top: 42%;
  left: 52%;
  transform: translate(-50%, -50%);
  background: #fff;
  color: var(--primary);
  font-weight: 700;
  border: 1.4px solid var(--primary);
  padding: 18px 38px;
  border-radius: 16px;
  box-shadow: var(--shadow);
  font-size: 1.08rem;
  z-index: 1000;
  display: none;
  animation: fadeIn 0.26s cubic-bezier(.4,1.3,.4,1);
}

/* ===== 현재 콘텐츠 정보 ===== */
#currentContentTitle {
  font-size: 1.17rem;
  color: var(--primary);
  font-weight: 800;
  background: #f5f6fd;
  padding: 14px 22px;
  border-radius: 12px;
  border: 1.3px solid var(--border);
  margin-bottom: 16px;
  margin-top: 23px;
  box-shadow: 0 2px 7px rgba(99,102,241,0.06);
  letter-spacing: -0.15px;
}

/* ===== 사이드바 ===== */
.sidebar h4 {
  font-size: 1.22rem;
  font-weight: 900;
  margin-bottom: 19px;
  color: var(--primary);
  letter-spacing: -0.8px;
}
.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.sidebar li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 21px;
  background: #fff;
  border-radius: 15px;
  margin-bottom: 13px;
  cursor: pointer;
  font-size: 1.05rem;
  border: 2px solid transparent;
  box-shadow: 0 1px 10px rgba(99,102,241,0.06);
  transition: all 0.21s cubic-bezier(.3,1.5,.5,1);
  outline: none;
  position: relative;
  min-height: 48px;
}
.sidebar li:hover, .sidebar li:focus {
  background: var(--primary-soft);
  border: 2px solid var(--primary);
  box-shadow: 0 2px 12px rgba(99,102,241,0.08);
  z-index: 2;
}
.sidebar li.active {
  background: var(--primary-soft);
  border-left: 7px solid var(--primary);
  font-weight: 900;
  color: var(--primary);
  z-index: 2;
  box-shadow: 0 3px 18px rgba(99,102,241,0.13);
}
.sidebar li.completed {
  background: var(--secondary-soft);
  color: var(--secondary);
  border-left: 7px solid var(--secondary);
  font-weight: 800;
}
.sidebar li.completed .progress-bar {
  background: var(--secondary);
}
.sidebar li.completed .badge {
  background: var(--secondary);
  color: #fff;
}

.sidebar li:focus-visible {
  outline: 2px solid var(--primary);
}

/* ===== 진도율(퍼센트+바) ===== */
.progress-percent {
  font-size: 1.03rem;
  font-weight: 900;
  color: var(--text-soft);
  margin-left: 13px;
  letter-spacing: -0.09px;
  display: flex;
  align-items: center;
  gap: 7px;
  min-width: 90px;
}
.progress-bar {
  width: 52px;
  height: 9px;
  background: var(--primary-soft);
  border-radius: 7px;
  margin-left: 10px;
  overflow: hidden;
  display: inline-block;
  vertical-align: middle;
  border: 1.2px solid var(--primary);
  box-shadow: 0 2px 9px rgba(99,102,241,0.09);
}
.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #6366f1 20%, #a5b4fc 100%);
  border-radius: 7px;
  transition: width 0.36s cubic-bezier(.4,2,.4,1);
}

/* ===== 수료 뱃지 ===== */
.badge {
  font-size: 1.07rem;
  margin-left: 14px;
  background: var(--primary-soft);
  color: var(--primary);
  padding: 5px 16px;
  border-radius: 20px;
  font-weight: 900;
  letter-spacing: -0.3px;
  border: 1.2px solid var(--primary);
  transition: background 0.23s, color 0.17s;
}

.sidebar li.completed .badge {
  background: var(--secondary);
  color: #fff;
  border: none;
}

/* ===== 반응형 ===== */
@media (max-width: 900px) {
  .video-section { padding: 16px 3vw 24px 3vw; border-radius: 0 0 var(--radius-xl) var(--radius-xl);}
  .sidebar { padding: 13px 3vw 11px 3vw;}
  .sidebar li { font-size: 0.99rem; padding: 11px 9px;}
  #currentContentTitle { margin-top: 10px;}
  .lecture-title { font-size: 1.45rem;}
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.96);}
  to { opacity: 1; transform: scale(1);}
}

</style>
</head>
<body>
<div class="video-section">
  <div class="lecture-title">${lecture.title}</div>

  <div id="loadingMessage">⏳ 콘텐츠 로딩 중입니다...</div>

  <video id="lectureVideo" controls></video>
  <div id="currentContentTitle">📺 콘텐츠를 준비 중입니다...</div>
  <div class="lecture-description">${lecture.description}</div>

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
//===== JS (lecturePlay.jsp 내 script 태그) =====

//JSP 컨텍스트 경로 안전하게 변수화
const CONTEXT_PATH = "<c:out value='${pageContext.request.contextPath}'/>";

//contents 생성: JSP EL만 사용, JS 템플릿 리터럴 사용 X
const contents = [
<c:forEach var="c" items="${contents}" varStatus="loop">
 {
   contentId: ${c.contentId},
   title: '<c:out value="${c.title}"/>',
   url: '<c:out value="${c.url}"/>',
   duration: ${c.duration > 0 ? c.duration : 300},
   progress: ${c.progressPercent}
 }<c:if test="${!loop.last}">,</c:if>
</c:forEach>
];

//기타 변수
const video = document.getElementById("lectureVideo");
const loadingMessage = document.getElementById("loadingMessage");
const titleDisplay = document.getElementById("currentContentTitle");
const contentList = document.getElementById("contentList");

let currentIndex = 0;
let hls = null;
let playedTime = 0;
let lastCheckTime = 0;
let lastSentPercent = 0;
let isPlaying = false;

//진도율 저장 AJAX
async function sendProgress(lectureId, contentId, percent) {
try {
 const res = await fetch(CONTEXT_PATH + "/lecture/progress/update", {
   method: "POST",
   headers: { "Content-Type": "application/json" },
   body: JSON.stringify({ lectureId, contentId, progress: percent })
 });
 const data = await res.json();
 return data.success;
} catch (e) {
 alert("❌ 진도율 저장 실패");
 return false;
}
}

//UI 진도/뱃지 갱신
function updateProgressBar(idx, percent) {
const li = contentList.children[idx];
let bar = li.querySelector('.progress-bar');
let fill = li.querySelector('.progress-bar-fill');
if (!bar) {
 bar = document.createElement('span');
 bar.className = 'progress-bar';
 fill = document.createElement('span');
 fill.className = 'progress-bar-fill';
 bar.appendChild(fill);
 li.querySelector('.progress-percent').appendChild(bar);
}
fill.style.width = percent + "%";
li.querySelector('.progress-percent').childNodes[0].nodeValue = percent + "% ";
if (percent === 100) {
 li.classList.add("completed");
 setBadge(li, "✔ 수료");
}
}
function setBadge(li, text) {
let badge = li.querySelector('.badge');
if (!badge) {
 badge = document.createElement('span');
 badge.className = 'badge';
 li.appendChild(badge);
}
badge.innerText = text;
}

function playContent(index, autoplay = true) {
if (!contents.length) return alert("시청 가능한 콘텐츠가 없습니다.");
const content = contents[index];
if (!content.url) return alert("콘텐츠 URL 없음");
if (content.duration < 3) return alert("영상 길이 부족");

currentIndex = index;
titleDisplay.innerText = "📺 " + content.title;
loadingMessage.style.display = 'block';

// HLS 지원시
if (window.Hls && Hls.isSupported()) {
 if (hls) hls.destroy();
 hls = new Hls();
 hls.loadSource(content.url);
 hls.attachMedia(video);
 hls.on(Hls.Events.MANIFEST_PARSED, () => {
   video.onloadedmetadata = () => {
     if (content.progress > 0 && video.duration)
       video.currentTime = Math.max(0, video.duration * (content.progress / 100));
     playedTime = video.duration * (content.progress / 100);
     lastSentPercent = content.progress;
     lastCheckTime = video.currentTime;
     loadingMessage.style.display = 'none';
     video.focus();
   };
   if (autoplay) video.play();
 });
} else if (video.canPlayType('application/vnd.apple.mpegurl')) {
 video.src = content.url;
 video.onloadedmetadata = () => {
   if (content.progress > 0 && video.duration)
     video.currentTime = Math.max(0, video.duration * (content.progress / 100));
   playedTime = video.duration * (content.progress / 100);
   lastSentPercent = content.progress;
   lastCheckTime = video.currentTime;
   loadingMessage.style.display = 'none';
   video.focus();
 };
 if (autoplay) video.play();
} else {
 alert("HLS 미지원 브라우저");
 loadingMessage.style.display = 'none';
}

// UI 업데이트
Array.from(contentList.children).forEach(li => li.classList.remove("active"));
contentList.children[index].classList.add("active");
updateProgressBar(index, content.progress);
}

//진도율 처리
video.addEventListener("play", () => { isPlaying = true; lastCheckTime = video.currentTime; });
video.addEventListener("pause", () => { isPlaying = false; });
video.addEventListener("seeking", () => { lastCheckTime = video.currentTime; });

video.addEventListener("timeupdate", async () => {
if (!isPlaying || video.seeking || video.paused) return;
const now = video.currentTime;
const delta = now - lastCheckTime;
if (delta > 0 && delta < 5) playedTime += delta;
lastCheckTime = now;

const content = contents[currentIndex];
const percent = Math.min(100, Math.round((playedTime / content.duration) * 100));
if (content.progress >= 100) return;
if (percent - lastSentPercent >= 5 || (percent === 100 && lastSentPercent < 100)) {
 lastSentPercent = percent;
 content.progress = percent;
 updateProgressBar(currentIndex, percent);
 await sendProgress("<c:out value='${lecture.lectureId}'/>", content.contentId, percent);
}
});

video.addEventListener("ended", async () => {
const content = contents[currentIndex];
if (content.progress < 100) {
 content.progress = 100;
 await sendProgress("<c:out value='${lecture.lectureId}'/>", content.contentId, 100);
 updateProgressBar(currentIndex, 100);
}
if (currentIndex < contents.length - 1) {
 playContent(currentIndex + 1, true);
} else {
 alert("🎉 강의 수강 완료!");
}
});

//최초 로드 시 진도/뱃지 UI 및 첫 챕터 재생
window.addEventListener("DOMContentLoaded", () => {
contents.forEach((content, idx) => {
 const el = document.getElementById("progress-" + content.contentId);
 if (el) el.innerText = content.progress + "%";
 updateProgressBar(idx, content.progress);
 if (content.progress === 100) {
   const li = document.getElementById("content-" + content.contentId);
   if (li) li.classList.add("completed");
 }
});
playContent(0, true);
});

</script>


</body>
</html>
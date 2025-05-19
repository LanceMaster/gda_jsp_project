document.addEventListener("DOMContentLoaded", () => {
  const video = document.getElementById("lecture-video");
  const seekBar = document.getElementById("seek-bar");
  const currentTimeDisplay = document.getElementById("current-time");

  const contentId = video.dataset.contentId;
  const userId = video.dataset.userId;
  const lectureId = video.dataset.lectureId;
  const progressUrl = video.dataset.progressUrl;

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
          body: `contentId=${contentId}&userId=${userId}&progress=${percent}&lectureId=${lectureId}`
        });
      }
    }
  }, 10000);

  // 재생 시간 표시
  video.addEventListener("timeupdate", () => {
    const minutes = Math.floor(video.currentTime / 60).toString().padStart(2, '0');
    const seconds = Math.floor(video.currentTime % 60).toString().padStart(2, '0');
    currentTimeDisplay.textContent = `${minutes}:${seconds}`;
    seekBar.value = video.currentTime;
  });

  // 초기 로딩 시 seekBar 최대값 설정
  video.addEventListener("loadedmetadata", () => {
    seekBar.max = video.duration;
  });

  // 사용자가 seekBar 움직일 때 영상 위치 조정
  seekBar.addEventListener("input", () => {
    video.currentTime = seekBar.value;
  });
});

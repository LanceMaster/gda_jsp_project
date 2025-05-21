package model.dto;

/**
 * ✅ 콘텐츠 정보 DTO - 각 강의에 연결된 비디오 콘텐츠 정보를 표현
 */
public class ContentDTO {

    private int contentId;         // 콘텐츠 PK
    private int lectureId;         // 연관 강의 ID
    private String title;          // 콘텐츠 제목
    private String url;            // .m3u8 스트리밍 URL
    private String type;           // 콘텐츠 유형 (예: VIDEO)
    private int duration;          // 재생 시간 (초)
    private int orderNo;           // 콘텐츠 순서
    private int progressPercent; // 진도율 (0~100)

    // ✅ Getter & Setter

    public int getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}

	public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}

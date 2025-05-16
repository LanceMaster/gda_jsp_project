package model.dto;

import java.time.LocalDateTime;

public class InquiryDTO {
    private int inquiryId;
    private int userId;
    private String type;
    private Integer lectureId;
    private String lectureTitle;
    private String title;
    private String content;
    private boolean isAnswered;
    private LocalDateTime createdAt;
    private String userName;  // 작성자 이름


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
	public int getInquiryId() {
		return inquiryId;
	}
	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getLectureId() {
		return lectureId;
	}
	public void setLectureId(Integer lectureId) {
		this.lectureId = lectureId;
	}
	public String getLectureTitle() {
		return lectureTitle;
	}
	public void setLectureTitle(String lectureTitle) {
		this.lectureTitle = lectureTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isAnswered() {
		return isAnswered;
	}
	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    // Getters and setters 생략 가능 (Lombok 사용 가능)
}

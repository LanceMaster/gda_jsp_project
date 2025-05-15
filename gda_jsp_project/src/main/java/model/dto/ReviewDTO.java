package model.dto;

import java.time.LocalDateTime;

/**
 * 📦 ReviewDTO - 강의 리뷰 데이터 전달 객체
 */
public class ReviewDTO {

    private int interactionId;   // 리뷰 고유 ID
    private int lectureId;       // 대상 강의 ID
    private String content;      // 리뷰 내용
    private int rating;          // 평점 (1~5)
    private int userId;          // 작성자 ID
    private String reviewer;     // 작성자 이름 (화면 노출용)
    private LocalDateTime createdAt; // 작성일

    // ✅ Getter/Setter
    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        this.interactionId = interactionId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

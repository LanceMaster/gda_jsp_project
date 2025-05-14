package model.dto;

import java.time.LocalDateTime;

/**
 * 📦 ReviewDTO - 리뷰 데이터 전달 객체
 */
public class ReviewDTO {
    private int reviewId;
    private int targetId;     // 강의 ID
    private String content;   // 리뷰 내용
    private int rating;       // 별점 (1~5)
    private int userId;       // 작성자 ID
    private String userName;  // 작성자 이름 (옵션)
    private LocalDateTime createdAt;

    // 📌 Getter/Setter
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

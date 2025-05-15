package model.dto;

import java.util.Date;

public class LectureReviewDTO {
    private int reviewId;
    private int lectureId;
    private int userId;
    private String userName; // 조인용
    private String content;
    private int rating;
    private Date createdAt;

    // 🧱 getter/setter
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getLectureId() { return lectureId; }
    public void setLectureId(int lectureId) { this.lectureId = lectureId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

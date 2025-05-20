package model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewDTO {
    private int interactionId;
    private int userId;
    private int lectureId;
    private String reviewer;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
    private String formattedCreatedAt;
    private String createdAtFormatted;

	public String getCreatedAtFormatted() {
		return createdAtFormatted;
	}
	public void setCreatedAtFormatted(String createdAtFormatted) {
		this.createdAtFormatted = createdAtFormatted;
	}
	public int getInteractionId() { return interactionId; }
    public void setInteractionId(int interactionId) { this.interactionId = interactionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getLectureId() { return lectureId; }
    public void setLectureId(int lectureId) { this.lectureId = lectureId; }

    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        this.formattedCreatedAt = createdAt != null
                ? createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : null;
    }

    public String getFormattedCreatedAt() { return formattedCreatedAt; }
    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }
}

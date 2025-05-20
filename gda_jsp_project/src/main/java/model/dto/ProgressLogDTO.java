package model.dto;

import java.time.LocalDateTime;

public class ProgressLogDTO {
    private int progressId;
    private int userId;
    private int contentId;
    private int progressPercent;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public int getProgressId() {
        return progressId;
    }
    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getContentId() {
        return contentId;
    }
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
    public int getProgressPercent() {
        return progressPercent;
    }
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }
    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

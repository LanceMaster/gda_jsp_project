package model.dto;

import java.time.LocalDateTime;

/**
 * 📄 InquiryDTO
 * - 강의 관련 문의 게시글 정보를 담는 DTO
 */
public class InquiryDTO {

    // ✅ 기본 필드 (DB 컬럼)
    private int inquiryId;         // 문의글 ID (PK)
    private int lectureId;         // 강의 ID (FK)
    private int userId;            // 작성자 ID (FK)
    private String title;          // 문의 제목
    private String content;        // 문의 내용
    private boolean isAnswered;    // 답변 여부
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시 ← 🔧 추가됨

    // ✅ 조회용 추가 필드 (JOIN 결과)
    private String lectureTitle;   // 강의 제목
    private String userName;       // 작성자 이름

    // ----- Getter / Setter -----

    public int getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(int inquiryId) {
        this.inquiryId = inquiryId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setAnswered(boolean answered) {
        isAnswered = answered;
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

    public String getLectureTitle() {
        return lectureTitle;
    }

    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

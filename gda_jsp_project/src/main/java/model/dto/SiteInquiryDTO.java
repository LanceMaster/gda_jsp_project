package model.dto;

import java.util.Date;

public class SiteInquiryDTO {
    // ✅ DB 컬럼 및 Mapper에서 SELECT되는 필드 전부 선언
    private int inquiryId;            // 문의 ID
    private int userId;               // 작성자 ID
    private String type = "SITE";     // 문의 유형 ('SITE' 고정)
    private Integer lectureId;        // 강의 ID (SITE 문의는 null)
    private String title;             // 문의 제목
    private String content;           // 문의 내용
    private boolean isAnswered;       // 답변 여부 (0/1)
    private Date createdAt;           // 작성일시
    private String userName;          // 작성자 이름 (users 테이블 JOIN 결과)

    // ✅ Getter / Setter

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

    // type은 setter를 막거나 SITE로 고정해도 됩니다.
    public void setType(String type) {
        this.type = type;
    }

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {         // ✅ 작성자 이름 getter
        return userName;
    }

    public void setUserName(String userName) {   // ✅ 작성자 이름 setter
        this.userName = userName;
    }

    // ✅ toString (디버깅용)
    @Override
    public String toString() {
        return "SiteInquiryDTO [inquiryId=" + inquiryId + ", userId=" + userId
                + ", type=" + type + ", lectureId=" + lectureId
                + ", title=" + title + ", content=" + content
                + ", isAnswered=" + isAnswered + ", createdAt=" + createdAt
                + ", userName=" + userName + "]";
    }
}

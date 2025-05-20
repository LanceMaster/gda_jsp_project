package model.dto;

import java.util.Date;
import java.util.List;

public class ProjectsDTO {
    // ✅ 모집 상태 ENUM 정의
    public enum RecruitStatus { RECRUITING, CLOSED }

    // ✅ 필드 선언
    private int projectId;                        // 프로젝트 ID
    private String title;                         // 제목
    private String description;                   // 설명
    private RecruitStatus recruitStatus = RecruitStatus.RECRUITING; // 기본 모집 상태
    private int viewCount = 0;                    // 기본 조회수 0
    private Date createdAt;                       // 생성 일시
    private int leaderId;                         // 리더 ID
    private String leaderName;                    // 리더 이름

    private List<TagDTO> tags;                    // ✅ 태그 목록 추가

    // ✅ Getter / Setter

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecruitStatus() {
        return recruitStatus.name();
    }

    public void setRecruitStatus(RecruitStatus recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    // ✅ 태그 Getter / Setter
    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    // ✅ 디버깅용 toString 메서드
    @Override
    public String toString() {
        return "ProjectsDTO [projectId=" + projectId + ", title=" + title + ", description=" + description
                + ", recruitStatus=" + recruitStatus + ", viewCount=" + viewCount
                + ", createdAt=" + createdAt + ", leaderId=" + leaderId
                + ", leaderName=" + leaderName + ", tags=" + tags + "]";
    }
}

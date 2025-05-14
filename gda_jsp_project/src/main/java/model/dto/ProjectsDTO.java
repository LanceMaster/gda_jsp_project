package model.dto;

import java.util.Date;

public class ProjectsDTO {
    public enum RecruitStatus { RECRUITING, CLOSED }

    private int projectId;
    private String title;
    private String description;
    private String thumbnail;
    private RecruitStatus recruitStatus = RecruitStatus.RECRUITING;  // 기본값 설정
    private int viewCount;
    private Date createdAt;
    private int leaderId;
	
    // Getters and Setters
    
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public RecruitStatus getRecruitStatus() {
		return recruitStatus;
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
}



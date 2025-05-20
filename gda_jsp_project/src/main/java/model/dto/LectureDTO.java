package model.dto;

import java.math.BigDecimal;

/**
 * ✅ 강의 정보 DTO (Data Transfer Object) - 등록, 조회, 수정, 목록 출력 모두 대응
 */
public class LectureDTO {

	// 📌 기본 강의 속성
	private int lectureId;
	private String title;
	private String description;
	private String thumbnail;
	private String category;
	private int price;
	private float avgRating;
	private int instructorId;
	private Integer reviewCount;
	private String curriculum;
	// enrollments 진행률 파라미터 추가
	private BigDecimal avgProgress; // 평균 진도율 (%)

	// ✅ Getter & Setter

	public String getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public float getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}

	public int getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(int instructorId) {
		this.instructorId = instructorId;
	}

	public BigDecimal getAvgProgress() {
		return avgProgress;
	}

	public void setAvgProgress(BigDecimal avgProgress) {
		this.avgProgress = avgProgress;
	}
}

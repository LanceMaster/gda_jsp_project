package model.dto;

import java.sql.Timestamp;

/**
 * 소셜 로그인 사용자 정보를 담는 DTO 클래스 테이블명: social_users
 */
public class NaverUserDTO {
	// 필드 정의
	private int id; // 고유 식별자
	private String socialId; // 소셜 플랫폼에서의 사용자 ID
	private String socialType; // 소셜 플랫폼 유형 (NAVER, KAKAO, GOOGLE 등)
	private String email; // 이메일
	private String name; // 이름
	private String nickname; // 닉네임
	private String profileImage; // 프로필 이미지 URL
	private Timestamp createdAt; // 생성 시간
	private Timestamp updatedAt; // 수정 시간
	private String role;
	private String phone;

	// 기본 생성자
	public NaverUserDTO() {
	}

	// 모든 필드를 포함한 생성자
	public NaverUserDTO(int id, String socialId, String socialType, String email, String name, String nickname,
			String profileImage, Timestamp createdAt, Timestamp updatedAt) {
		this.id = id;
		this.socialId = socialId;
		this.socialType = socialType;
		this.email = email;
		this.name = name;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// 신규 사용자 등록용 생성자
	public NaverUserDTO(String socialId, String socialType, String email, String name, String nickname,
			String profileImage) {
		this.socialId = socialId;
		this.socialType = socialType;
		this.email = email;
		this.name = name;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	// Getter 및 Setter 메소드
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	// toString 메소드
	@Override
	public String toString() {
		return "SocialUserDTO{" + "id=" + id + ", socialId='" + socialId + '\'' + ", socialType='" + socialType + '\''
				+ ", email='" + email + '\'' + ", name='" + name + '\'' + ", nickname='" + nickname + '\''
				+ ", profileImage='" + profileImage + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ '}';
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
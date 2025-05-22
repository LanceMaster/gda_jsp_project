package model.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class UserDTO {
	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", email=" + email + ", password=" + password + ", name=" + name
				+ ", phone=" + phone + ", birthdate=" + birthdate + ", profileImage=" + profileImage + ", role=" + role
				+ ", deletedAt=" + deletedAt + ", bio=" + bio + ", resume=" + resume + ", isVerified=" + isVerified
				+ ", isDeleted=" + isDeleted + ", agreedTerms=" + agreedTerms + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", lastLoginAt=" + lastLoginAt + "]";
	}

	private int userId; // user_id
	private String email;
	private String password;
	private String name;
	private String phone;
	private Date birthdate;
	private String profileImage; // profile_image
	private String role;
//	private LocalDateTime deletedAt; // ✅ 추가된 필드
	private Date deletedAt;
	private String bio;
	private String resume;
	private boolean isVerified; // is_verified
	private boolean isDeleted; // is_deleted
	private boolean agreedTerms; // agreed_terms
	private Date createdAt; // created_at
	private Date updatedAt; // updated_at
	private Date lastLoginAt; // last_login_at
	// === Getters & Setters ===

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isAgreedTerms() {
		return agreedTerms;
	}

	public void setAgreedTerms(boolean agreedTerms) {
		this.agreedTerms = agreedTerms;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(Date lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

}

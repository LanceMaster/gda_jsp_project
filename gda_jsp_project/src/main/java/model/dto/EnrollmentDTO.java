package model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnrollmentDTO {
	private int enrollmentId; // 수강 등록 고유 ID
	private String status; // 수강 상태: IN_PROGRESS, COMPLETED, CANCELLED
	private String paymentStatus; // 결제 상태: PENDING, PAID, CANCELLED
	private String paymentMethod; // 결제 수단: CARD, BANK_TRANSFER, KAKAO, NAVER
	private String discountCode; // 할인 코드
	private Integer amountPaid; // 결제 금액
	private String orderId; // 주문 번호
	private LocalDateTime enrolledAt; // 수강 등록 일시
	private int userId; // 수강 사용자 ID
	private int lectureId; // 수강 강의 ID
	private BigDecimal avgProgress; // 평균 진도율 (%)

	// --- Getters and Setters ---
	public int getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(int enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public Integer getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Integer amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getEnrolledAt() {
		return enrolledAt;
	}

	public void setEnrolledAt(LocalDateTime enrolledAt) {
		this.enrolledAt = enrolledAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public BigDecimal getAvgProgress() {
		return avgProgress;
	}

	public void setAvgProgress(BigDecimal avgProgress) {
		this.avgProgress = avgProgress;
	}
}

package service;

import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

import java.util.List;

/**
 * 📘 ReviewService
 * - 강의 리뷰 조회 및 등록 처리 담당
 */
public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }

    /**
     * ✅ 강의 ID로 모든 리뷰 조회
     */
    public List<ReviewDTO> getReviewsByLectureId(int lectureId) {
        return reviewDAO.getReviewsByLectureId(lectureId);
    }

    /**
     * ✅ 리뷰 등록
     */
    public void insertReview(ReviewDTO reviewDTO) {
        reviewDAO.insertReview(reviewDTO);
    }

    /**
     * ✅ 리뷰 등록 (호출용 alias)
     */
    public void addReview(ReviewDTO dto) {
        reviewDAO.insertReview(dto);
    }

    /**
     * ✅ 해당 사용자가 강의를 수강했는지 확인
     */
    public boolean hasEnrolled(int userId, int lectureId) {
        return reviewDAO.hasEnrolled(userId, lectureId);
    }

    /**
     * ✅ 해당 사용자가 해당 강의에 리뷰를 이미 작성했는지 확인
     */
    public boolean hasReviewed(int userId, int lectureId) {
        return reviewDAO.hasReviewed(userId, lectureId);
    }

    /**
     * ✅ 수강 완료 여부 확인 (진도율 100% 기준)
     */
    public boolean hasCompletedWithFullProgress(int userId, int lectureId) {
        return reviewDAO.hasCompletedWithFullProgress(userId, lectureId);
    }

    /**
     * ✅ 리뷰 작성 가능 여부 (수강 완료 + 미작성 상태)
     */
    public boolean canWriteReview(int userId, int lectureId) {
        boolean hasEnrolled = hasCompletedWithFullProgress(userId, lectureId);
        boolean hasReviewed = hasReviewed(userId, lectureId);
        return hasEnrolled && !hasReviewed;
    }
}

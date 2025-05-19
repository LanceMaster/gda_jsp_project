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
    public void addReview(ReviewDTO dto) {
        reviewDAO.insertReview(dto);
    }
    
    
    /**
     * ✅ 리뷰 등록
     */
    public void insertReview(ReviewDTO reviewDTO) {
        reviewDAO.insertReview(reviewDTO);
    }
}

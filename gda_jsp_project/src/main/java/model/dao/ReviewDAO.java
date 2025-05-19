package model.dao;

import model.dto.ReviewDTO;
import model.mapper.ReviewMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

/**
 * ✅ ReviewDAO
 * - 리뷰 조회, 작성, 상태 검사 관련 DAO
 * - 세션 수동 관리로 자원 누수 방지
 */
public class ReviewDAO {

    /**
     * ✅ 강의별 리뷰 전체 조회
     */
    public List<ReviewDTO> getReviewsByLectureId(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            return mapper.getReviewsByLectureId(lectureId);
        }
    }

    /**
     * ✅ 리뷰 등록 (커밋 포함)
     */
    public void insertReview(ReviewDTO dto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            mapper.insertReview(dto);
            session.commit();
        }
    }

    /**
     * ✅ 수강 여부 확인 (enrollments 테이블)
     */
    public boolean hasEnrolled(int userId, int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            return mapper.hasEnrolled(userId, lectureId);
        }
    }

    /**
     * ✅ 리뷰 작성 여부 확인 (user_interactions 테이블)
     */
    public boolean hasReviewed(int userId, int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            return mapper.hasReviewed(userId, lectureId);
        }
    }

    /**
     * ✅ 수강 진도율 100% 완료 여부 (progress_logs 기준)
     */
    public boolean hasCompletedWithFullProgress(int userId, int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            return mapper.hasCompletedWithFullProgress(userId, lectureId);
        }
    }

    /**
     * ✅ 강의 평균 평점 갱신 (lectures 테이블)
     */
    public void updateLectureRating(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            mapper.updateLectureRating(lectureId);
        }
    }
}

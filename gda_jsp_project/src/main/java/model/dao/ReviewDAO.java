package model.dao;

import model.dto.ReviewDTO;
import model.mapper.ReviewMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

/**
 * ✅ 리뷰 DAO - 리뷰 조회 및 등록 기능 제공
 */
public class ReviewDAO {

    /**
     * 📌 강의 ID로 리뷰 목록 조회
     */
    public List<ReviewDTO> getReviewsByLectureId(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            return mapper.getReviewsByLectureId(lectureId);
        }
    }

    /**
     * 📌 리뷰 등록
     */
    public void insertReview(ReviewDTO dto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            mapper.insertReview(dto);
        }
    }

    /**
     * 📌 리뷰 평균 평점 업데이트 (강의 테이블 반영용)
     */
    public void updateLectureRating(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            ReviewMapper mapper = session.getMapper(ReviewMapper.class);
            mapper.updateLectureRating(lectureId);
        }
    }
} 
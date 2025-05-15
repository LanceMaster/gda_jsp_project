package model.mapper;

import model.dto.ReviewDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReviewMapper {

    /**
     * 📌 특정 강의의 리뷰 목록 조회 (유저 이름 포함)
     */
    @Select("""
        SELECT r.review_id, r.target_id, r.content, r.rating, r.user_id, r.created_at,
               u.name AS user_name
        FROM reviews r
        JOIN users u ON r.user_id = u.user_id
        WHERE r.target_id = #{lectureId}
        ORDER BY r.created_at DESC
    """)
    List<ReviewDTO> getReviewsByLectureId(@Param("lectureId") int lectureId);

    /**
     * 📌 리뷰 등록
     */
    @Insert("""
        INSERT INTO reviews (target_id, content, rating, user_id, created_at)
        VALUES (#{targetId}, #{content}, #{rating}, #{userId}, NOW())
    """)
    void insertReview(ReviewDTO dto);

    /**
     * 📌 해당 강의의 평균 평점을 계산하여 lectures 테이블에 반영
     */
    @Update("""
        UPDATE lectures l
        SET l.avg_rating = (
            SELECT IFNULL(ROUND(AVG(r.rating), 1), 0)
            FROM reviews r
            WHERE r.target_id = #{lectureId}
        )
        WHERE l.lecture_id = #{lectureId}
    """)
    void updateLectureRating(@Param("lectureId") int lectureId);
} 

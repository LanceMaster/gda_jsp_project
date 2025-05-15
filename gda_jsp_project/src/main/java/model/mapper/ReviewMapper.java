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
		    SELECT 
		        interaction_id AS reviewId,
		        target_id AS lectureId,
		        title AS reviewer,
		        rating,
		        content,
		        created_at
		    FROM user_interactions
		    WHERE target_type = 'LECTURE'
		      AND target_id = #{lectureId}
		      AND interaction_kind = 'FEEDBACK'
		    ORDER BY created_at DESC
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

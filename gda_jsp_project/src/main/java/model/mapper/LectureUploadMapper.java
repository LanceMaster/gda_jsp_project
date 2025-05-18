package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 📌 MyBatis Mapper - 강의 및 콘텐츠 관련 SQL 정의
 * - 어노테이션 기반 SQL 매핑
 * - LectureDAO가 이 Mapper를 통해 DB 접근
 */
public interface LectureUploadMapper {

    /**
     * ✅ 1. 강의 등록
     */
    @Insert("""
        INSERT INTO lectures
        (title, description, thumbnail, category, price, status, avg_rating, published_at, created_at, updated_at, instructor_id)
        VALUES
        (#{title}, #{description}, #{thumbnail}, #{category}, #{price}, 'PUBLISHED', NULL, NOW(), NOW(), NOW(), #{instructorId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "lectureId")
    void insertLecture(LectureDTO lectureDTO);

    /**
     * ✅ 2. 콘텐츠 등록
     */
    @Insert("""
        INSERT INTO lecture_contents
        (lecture_id, title, url, type, order_no, duration, created_at)
        VALUES
        (#{lectureId}, #{title}, #{url}, #{type}, #{orderNo}, #{duration}, NOW())
    """)
    void insertContent(ContentDTO contentDTO);

    /**
     * ✅ 3. 강의 상세 조회
     */
    @Select("""
        SELECT
            lecture_id AS lectureId,
            title,
            description,
            thumbnail,
            category,
            price,
            avg_rating AS avgRating,
            instructor_id AS instructorId,
            curriculum
        FROM lectures
        WHERE lecture_id = #{lectureId}
    """)
    LectureDTO getLectureById(@Param("lectureId") int lectureId);

    /**
     * ✅ 4. 첫 콘텐츠 조회 (재생용)
     */
    @Select("""
        SELECT
            content_id AS contentId,
            lecture_id AS lectureId,
            title,
            url,
            type,
            order_no AS orderNo,
            duration,
            created_at AS createdAt
        FROM lecture_contents
        WHERE lecture_id = #{lectureId}
        ORDER BY order_no ASC
        LIMIT 1
    """)
    ContentDTO getFirstContentByLectureId(@Param("lectureId") int lectureId);

    /**
     * ✅ 5. 강의 평점 평균 업데이트
     */
    @Update("""
        UPDATE lectures
        SET avg_rating = (
            SELECT ROUND(AVG(rating), 1)
            FROM user_interactions
            WHERE target_type = 'LECTURE'
              AND target_id = #{lectureId}
              AND interaction_kind = 'FEEDBACK'
        )
        WHERE lecture_id = #{lectureId}
    """)
    void updateLectureRating(@Param("lectureId") int lectureId);

    /**
     * ✅ 6. 동적 정렬 / 필터 (XML or Provider 방식)
     * ※ XML 또는 @SelectProvider로 분리 가능
     */
    List<LectureDTO> getLecturesFilteredSorted(Map<String, Object> params);
}

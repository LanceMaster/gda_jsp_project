package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface LectureMapper {

    // ✅ 1. 강의 등록
    @Insert("""
        INSERT INTO lectures
        (title, description, thumbnail, category, price, status, avg_rating, published_at, created_at, updated_at, instructor_id)
        VALUES
        (#{title}, #{description}, #{thumbnail}, #{category}, #{price}, 'DRAFT', NULL, NULL, NOW(), NOW(), #{instructorId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "lectureId")
    int insertLecture(LectureDTO lectureDTO);

    // ✅ 2. 콘텐츠 등록
    @Insert("""
        INSERT INTO lecture_contents
        (lecture_id, type, title, url, duration, order_no, created_at)
        VALUES
        (#{lectureId}, #{type}, #{title}, #{url}, #{duration}, #{orderNo}, NOW())
    """)
    int insertContent(ContentDTO contentDTO);

    // ✅ 3. 전체 강의 목록 조회
    @Select("""
        SELECT 
            l.lecture_id AS lectureId,
            l.title,
            l.description,
            l.thumbnail,
            l.category,
            l.price,
            l.avg_rating AS avgRating,
            l.instructor_id AS instructorId,
            l.published_at AS publishedAt,
            l.created_at AS createdAt,
            l.updated_at AS updatedAt
        FROM lectures l
        WHERE l.status = 'PUBLISHED'
        ORDER BY l.created_at DESC
    """)
    List<LectureDTO> getAllLectures();

    // ✅ 4. 카테고리별 조회
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
            published_at AS publishedAt,
            created_at AS createdAt,
            updated_at AS updatedAt
        FROM lectures
        WHERE category = #{category}
        AND status = 'PUBLISHED'
        ORDER BY created_at DESC
    """)
    List<LectureDTO> selectByCategory(@Param("category") String category);

    // ✅ 5. 키워드 검색 (제목 + 설명 LIKE)
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
            published_at AS publishedAt,
            created_at AS createdAt,
            updated_at AS updatedAt
        FROM lectures
        WHERE (title LIKE CONCAT('%', #{keyword}, '%') 
            OR description LIKE CONCAT('%', #{keyword}, '%'))
        AND status = 'PUBLISHED'
        ORDER BY created_at DESC
    """)
    List<LectureDTO> searchByKeyword(@Param("keyword") String keyword);

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
    	        published_at AS publishedAt,
    	        created_at AS createdAt,
    	        updated_at AS updatedAt
    	    FROM lectures
    	    WHERE lecture_id = #{lectureId}
    	""")
    	LectureDTO getLectureById(@Param("lectureId") int lectureId);


    // ✅ 7. 첫 번째 콘텐츠 조회 (order_no = 1)
    @Select("""
        SELECT 
            content_id AS contentId,
            lecture_id AS lectureId,
            type,
            title,
            url,
            duration,
            order_no AS orderNo,
            created_at AS createdAt
        FROM lecture_contents
        WHERE lecture_id = #{lectureId}
        ORDER BY order_no ASC
        LIMIT 1
    """)
    ContentDTO getFirstContentByLectureId(@Param("lectureId") int lectureId);

    @Select("""
    	    SELECT title FROM lectures WHERE lecture_id = #{lectureId}
    	""")
    	String selectTitleById(@Param("lectureId") int lectureId);

    
    // ✅ 8. 강의 평균 평점 갱신
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

    // ✅ 9. 동적 정렬 + 카테고리 필터 (XML에 정의됨)
    List<LectureDTO> getLecturesFilteredSorted(Map<String, Object> params);
}

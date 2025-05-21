package model.mapper;

import model.dto.LectureManagementLectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface LectureManagementLectureMapper {

    /**
     * 1. 강사의 강의 목록을 조건(검색/카테고리/상태/정렬)별로 조회
     */
    @Select({
        "<script>",
        "SELECT * FROM lectures",
        "WHERE instructor_id = #{instructorId}",
        "<if test='search != null and search != \"\"'>",
        "AND (title LIKE CONCAT('%', #{search}, '%') OR description LIKE CONCAT('%', #{search}, '%'))",
        "</if>",
        "<if test='category != null and category != \"\"'>",
        "AND category = #{category}",
        "</if>",
        "<if test='status != null and status != \"\"'>",
        "AND status = #{status}",
        "</if>",
        "<choose>",
        "  <when test='sort == \"price_asc\"'>ORDER BY price ASC</when>",
        "  <when test='sort == \"price_desc\"'>ORDER BY price DESC</when>",
        "  <when test='sort == \"rating_desc\"'>ORDER BY avg_rating DESC</when>",
        "  <otherwise>ORDER BY created_at DESC</otherwise>",
        "</choose>",
        "</script>"
    })
    List<LectureManagementLectureDTO> selectLecturesByInstructor(Map<String, Object> params);

    /**
     * 2. 강의의 개별 필드를 동적으로 업데이트
     * - fieldName은 사전에 허용된 컬럼만 처리(매퍼 XML 방식이면 <choose>로 화이트리스트!)
     */
    @Update({
        "<script>",
        "UPDATE lectures",
        "<set>",
        "  <if test='field == \"title\"'>title = #{value},</if>",
        "  <if test='field == \"price\"'>price = #{value},</if>",
        "  <if test='field == \"category\"'>category = #{value},</if>",
        "  <if test='field == \"description\"'>description = #{value},</if>",
        "  <if test='field == \"status\"'>status = #{value},</if>",
        "  <if test='field == \"thumbnail_url\"'>thumbnail_url = #{value},</if>",
        "  <if test='field == \"avg_rating\"'>avg_rating = #{value},</if>",
        "  <if test='field == \"tags\"'>tags = #{value},</if>",
        // 필요한 필드는 여기에 추가
        "</set>",
        "WHERE lecture_id = #{lectureId}",
        "</script>"
    })
    int updateLectureField(Map<String, Object> params);

    /**
     * 3. 강의 공개 상태 변경
     */
    @Update("UPDATE lectures SET status = #{status} WHERE lecture_id = #{lectureId}")
    int updateLectureStatus(@Param("lectureId") Long lectureId, @Param("status") String status);

    /**
     * 4. 강의 삭제
     */
    @Delete("DELETE FROM lectures WHERE lecture_id = #{lectureId}")
    int deleteLectureById(@Param("lectureId") Long lectureId);

    /**
     * 5. 전체 카테고리 목록 조회 (중복 제거)
     */
    @Select("SELECT DISTINCT category FROM lectures ORDER BY category ASC")
    List<String> selectAllCategories();
}

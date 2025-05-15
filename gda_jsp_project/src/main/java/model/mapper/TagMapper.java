package model.mapper;

import model.dto.TagDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 🏷️ TagMapper
 * - 태그 관련 MyBatis 매퍼 인터페이스
 */
public interface TagMapper {

    /**
     * ✅ 특정 강의에 연결된 태그 목록 조회
     */
    @Select("""
        SELECT t.tag_id AS tagId,
               t.name     AS name
        FROM tag_mappings tm
        JOIN tags t ON tm.tag_id = t.tag_id
        WHERE tm.target_type = 'LECTURE'
          AND tm.target_id = #{lectureId}
    """)
    List<TagDTO> getTagsByLectureId(@Param("lectureId") int lectureId);
}

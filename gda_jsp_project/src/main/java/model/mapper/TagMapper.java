package model.mapper;

import model.dto.TagDTO;

import org.apache.ibatis.annotations.Insert;
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
 
    
    /**
     * ✅ 특정 프로젝트에 연결된 태그 목록 조회
     */
    @Select("""
        SELECT t.tag_id AS tagId,
               t.name     AS name
        FROM tag_mappings tm
        JOIN tags t ON tm.tag_id = t.tag_id
        WHERE tm.target_type = 'PROJECT'
          AND tm.target_id = #{projectId}
    """)
    List<TagDTO> getTagsByProjectId(@Param("projectId") int projectId);

    /**
     * ✅ 전체 태그 목록 조회
     */
    @Select("SELECT tag_id AS tagId, name FROM tags")
    List<TagDTO> getAllTags();
    
    
    /**
     * ✅ 태그 매핑 삽입
     */
    @Insert("INSERT INTO tag_mappings (target_id, target_type, tag_id) VALUES (#{targetId}, #{targetType}, #{tagId})")
    void insertMapping(@Param("targetId") int targetId,
                       @Param("targetType") String targetType,
                       @Param("tagId") int tagId);
    
    @Select("""
            SELECT t.tag_id AS tagId, t.name AS name
            FROM tag_mappings tm
            JOIN tags t ON tm.tag_id = t.tag_id
            WHERE tm.target_type = 'LECTURE'
            GROUP BY t.tag_id
            ORDER BY COUNT(*) DESC
            LIMIT #{limit}
        """)
        List<TagDTO> getTopTags(@Param("limit") int limit);


}



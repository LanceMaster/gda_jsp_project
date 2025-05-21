// ✅ 4. LecturePlayMapper.java
package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureDTO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LecturePlayMapper {
    @Select("SELECT * FROM lectures WHERE lecture_id = #{lectureId}")
    LectureDTO selectLectureById(int lectureId);

    @Select("""
        SELECT * FROM lecture_contents
        WHERE lecture_id = #{lectureId}
        ORDER BY order_no ASC
    """)
    List<ContentDTO> selectContentsByLectureId(int lectureId);
    
    @Select("""
    	    SELECT c.*, IFNULL(p.progress_percent, 0) AS progressPercent
    	    FROM lecture_contents c
    	    LEFT JOIN progress_logs p ON c.content_id = p.content_id AND p.user_id = #{userId}
    	    WHERE c.lecture_id = #{lectureId}
    	    ORDER BY c.order_no ASC
    	""")
    	List<ContentDTO> selectContentsWithProgress(@Param("lectureId") int lectureId, @Param("userId") int userId);


}
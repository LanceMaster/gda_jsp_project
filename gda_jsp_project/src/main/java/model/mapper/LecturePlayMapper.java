// ✅ 4. LecturePlayMapper.java
package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
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
}
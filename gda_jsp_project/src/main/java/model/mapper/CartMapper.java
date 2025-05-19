package model.mapper;

import model.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Insert("""
        INSERT IGNORE INTO carts (user_id, lecture_id, added_at)
        VALUES (#{userId}, #{lectureId}, NOW())
    """)
    void insertCart(@Param("userId") int userId, @Param("lectureId") int lectureId);

    @Delete("""
        DELETE FROM carts
        WHERE user_id = #{userId} AND lecture_id = #{lectureId}
    """)
    void deleteCartItem(@Param("userId") int userId, @Param("lectureId") int lectureId);

    @Select("""
    	    SELECT 
    	        l.lecture_id AS lectureId,
    	        l.title,
    	        l.thumbnail,
    	        l.price,
    	        l.category,
    	        l.avg_rating AS avgRating,
    	        (SELECT COUNT(*) FROM user_interactions 
    	         WHERE target_type='LECTURE' AND target_id=l.lecture_id) AS reviewCount
    	    FROM carts c
    	    JOIN lectures l ON c.lecture_id = l.lecture_id
    	    WHERE c.user_id = #{userId}
    	""")
    	List<LectureDTO> selectCartLectures(@Param("userId") int userId);


}

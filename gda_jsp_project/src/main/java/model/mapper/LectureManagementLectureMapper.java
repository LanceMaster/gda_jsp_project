package model.mapper;

import model.dto.LectureManagementLectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LectureManagementLectureMapper {

    @Select("""
        <script>
        SELECT l.lecture_id, l.title, l.description, l.category, l.price, 
               l.status, l.thumbnail, IFNULL(AVG(r.rating), 0) AS avgRating
        FROM lectures l
        LEFT JOIN reviews r ON l.lecture_id = r.lecture_id
        WHERE l.instructor_id = #{instructorId}
        <if test="search != null">
            AND (l.title LIKE CONCAT('%', #{search}, '%') OR l.description LIKE CONCAT('%', #{search}, '%'))
        </if>
        <if test="category != null">
            AND l.category = #{category}
        </if>
        <if test="status != null">
            AND l.status = #{status}
        </if>
        GROUP BY l.lecture_id
        <choose>
            <when test="sort == 'price_desc'">ORDER BY l.price DESC</when>
            <when test="sort == 'price_asc'">ORDER BY l.price ASC</when>
            <when test="sort == 'rating_desc'">ORDER BY avgRating DESC</when>
            <otherwise>ORDER BY l.created_at DESC</otherwise>
        </choose>
        </script>
    """)
    List<LectureManagementLectureDTO> selectLecturesByInstructorFiltered(
        @Param("instructorId") Long instructorId,
        @Param("search") String search,
        @Param("category") String category,
        @Param("status") String status,
        @Param("sort") String sort
    );

    @Select("SELECT DISTINCT category FROM lectures ORDER BY category")
    List<String> selectAllCategories();
    
    @Update("""
    	    <script>
    	    UPDATE lectures
    	    <set>
    	        <if test="field == 'title'">title = #{value}</if>
    	        <if test="field == 'description'">description = #{value}</if>
    	        <if test="field == 'category'">category = #{value}</if>
    	        <if test="field == 'price'">price = #{value}</if>
    	    </set>
    	    WHERE lecture_id = #{lectureId}
    	    </script>
    	""")
    	int updateLectureField(@Param("lectureId") Long lectureId,
    	                       @Param("field") String field,
    	                       @Param("value") String value);
    
    @Update("UPDATE lectures SET status = #{status} WHERE lecture_id = #{lectureId}")
    int updateStatus(@Param("lectureId") Long lectureId, @Param("status") String status);

    
    @Delete("DELETE FROM lectures WHERE lecture_id = #{lectureId}")
    int deleteLecture(@Param("lectureId") Long lectureId);
}

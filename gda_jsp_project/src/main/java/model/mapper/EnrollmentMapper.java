package model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnrollmentMapper {

    @Select("SELECT 1 FROM enrollments WHERE user_id = #{userId} AND lecture_id = #{lectureId} LIMIT 1")
    Integer isUserEnrolled(@Param("userId") int userId, @Param("lectureId") int lectureId);

}

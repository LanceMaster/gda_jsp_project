package model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EnrollmentMapper {

    @Select("SELECT 1 FROM enrollments WHERE user_id = #{userId} AND lecture_id = #{lectureId} LIMIT 1")
    Integer isUserEnrolled(@Param("userId") int userId, @Param("lectureId") int lectureId);
    
    @Update("""
            UPDATE enrollments
            SET status = 'COMPLETED',
                completed_at = NOW()
            WHERE user_id = #{userId}
              AND lecture_id = #{lectureId}
        """)
        void markLectureAsCompleted(@Param("userId") int userId, @Param("lectureId") int lectureId);
    
    @Insert("""
			INSERT INTO enrollments (user_id, lecture_id, amount_paid, payment_method)
			VALUES (#{userId}, #{lectureId}, #{amountPaid}, #{paymentMethod})
		""")
		int insertEnrollment(@Param("userId") int userId, @Param("lectureId") int lectureId,
							  @Param("amountPaid") int amountPaid, @Param("paymentMethod") String paymentMethod);
    
}

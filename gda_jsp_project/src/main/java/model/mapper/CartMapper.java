package model.mapper;

import model.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    // ✅ 장바구니 강의 목록 조회 (강의 정보 + 리뷰 수 포함)
    @Select("""
        SELECT l.lecture_id AS lectureId, l.title, l.thumbnail, l.price, l.category,
               l.avg_rating AS avgRating,
               (SELECT COUNT(*) FROM user_interactions 
                WHERE target_type='LECTURE' AND target_id=l.lecture_id) AS reviewCount
        FROM carts c
        JOIN lectures l ON c.lecture_id = l.lecture_id
        WHERE c.user_id = #{userId}
    """)
    List<LectureDTO> selectCartLectures(@Param("userId") int userId);

    // ✅ 장바구니에 강의 추가 (기존 insertCartItem → insertCart로 통일)
    @Insert("""
        INSERT INTO carts (user_id, lecture_id)
        VALUES (#{userId}, #{lectureId})
    """)
    void insertCart(@Param("userId") int userId, @Param("lectureId") int lectureId);

    // ✅ 장바구니에서 강의 제거
    @Delete("""
        DELETE FROM carts
        WHERE user_id = #{userId} AND lecture_id = #{lectureId}
    """)
    void deleteCartItem(@Param("userId") int userId, @Param("lectureId") int lectureId);

    // ✅ 장바구니에 동일 강의가 존재하는지 확인 (중복 체크)
    @Select("""
        SELECT COUNT(*) 
        FROM carts 
        WHERE user_id = #{userId} AND lecture_id = #{lectureId}
    """)
    int existsInCart(@Param("userId") int userId, @Param("lectureId") int lectureId);
}

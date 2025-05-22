package model.mapper;

import model.dto.ContentDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
public interface ProgressMapper {

    // ✅ 진도율 저장 또는 업데이트 (upsert 단일 메서드만 사용)
    @Insert("""
        INSERT INTO progress_logs (
            user_id, content_id, progress_percent, created_at, updated_at, last_accessed_at
        )
        VALUES (
            #{userId}, #{contentId}, #{percent}, NOW(), NOW(), NOW()
        )
        ON DUPLICATE KEY UPDATE
            progress_percent = CASE
                WHEN progress_percent < #{percent} THEN #{percent}
                ELSE progress_percent
            END,
            updated_at = NOW(),
            last_accessed_at = NOW()
    """)
    void upsertProgress(@Param("userId") int userId,
                        @Param("contentId") int contentId,
                        @Param("percent") int percent);


    // ✅ 강의 수강 완료 처리 (진도율이 전부 100%일 때)
    @Update("""
        UPDATE enrollments
        SET status = 'COMPLETED'
        WHERE user_id = #{userId}
          AND lecture_id = #{lectureId}
          AND status != 'COMPLETED'
    """)
    void markEnrollmentComplete(@Param("userId") int userId,
                                @Param("lectureId") int lectureId);

    // ✅ 해당 콘텐츠의 소속 강의 ID 조회
    @Select("""
        SELECT lecture_id
        FROM contents
        WHERE content_id = #{contentId}
    """)
    Integer selectLectureIdByContentId(@Param("contentId") int contentId);

    // ✅ 특정 강의의 미완료 콘텐츠 수 조회
    @Select("""
        SELECT COUNT(*) 
        FROM lecture_contents c
        LEFT JOIN progress_logs p 
          ON c.content_id = p.content_id AND p.user_id = #{userId}
        WHERE c.lecture_id = #{lectureId}
          AND (p.progress_percent IS NULL OR p.progress_percent < 100)
    """)
    int countIncompleteContents(@Param("lectureId") int lectureId,
                                @Param("userId") int userId);

    // ✅ 전체 콘텐츠 진도율 맵 조회 (초기 화면 출력용)
    @Select("""
        SELECT c.content_id, IFNULL(p.progress_percent, 0) AS progress
        FROM contents c
        LEFT JOIN progress_logs p 
          ON c.content_id = p.content_id AND p.user_id = #{userId}
        WHERE c.lecture_id = #{lectureId}
    """)
    @MapKey("content_id")
    Map<Integer, Integer> selectProgressMap(@Param("userId") int userId,
                                            @Param("lectureId") int lectureId);

}

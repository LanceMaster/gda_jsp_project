package model.mapper;

import model.dto.ContentDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ProgressMapper {

    // ✅ 1. 진도 기록 존재 여부 확인
    @Select("""
        SELECT progress_id
        FROM progress_logs
        WHERE user_id = #{userId} AND content_id = #{contentId}
    """)
    Integer checkExist(@Param("userId") int userId, @Param("contentId") int contentId);

    // ✅ 2. 신규 진도 기록
    @Insert("""
        INSERT INTO progress_logs (user_id, content_id, progress_percent, created_at, updated_at, last_accessed_at)
        VALUES (#{userId}, #{contentId}, #{percent}, NOW(), NOW(), NOW())
    """)
    void insertProgress(@Param("userId") int userId,
                        @Param("contentId") int contentId,
                        @Param("percent") int percent);

    // ✅ 3. 기존 진도 기록 갱신
    @Update("""
        UPDATE progress_logs
        SET progress_percent = #{percent},
            updated_at = NOW(),
            last_accessed_at = NOW()
        WHERE user_id = #{userId} AND content_id = #{contentId}
    """)
    void updateProgress(@Param("userId") int userId,
                        @Param("contentId") int contentId,
                        @Param("percent") int percent);

    // ✅ 4. 특정 강의의 전체 콘텐츠 조회 (예: lectureId 기반)
    @Select("""
        SELECT content_id, lecture_id, title, url, duration, sequence
        FROM contents
        WHERE lecture_id = #{lectureId}
        ORDER BY sequence ASC
    """)
    List<ContentDTO> selectAllContents(@Param("lectureId") int lectureId);
}

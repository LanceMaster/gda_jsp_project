package model.mapper;

import org.apache.ibatis.annotations.*;

/**
 * 🧭 ProgressMapper
 * - 사용자 강의 콘텐츠 진도율(progress_logs) 관리
 */
public interface ProgressMapper {

    /**
     * ✅ 기존 기록 존재 여부 확인
     * - 해당 유저가 해당 콘텐츠의 진도 기록을 남겼는지 확인
     * @return progress_id (존재 시) 또는 null
     */
    @Select("""
        SELECT progress_id
        FROM progress_logs
        WHERE user_id = #{userId}
          AND content_id = #{contentId}
    """)
    Integer checkExist(@Param("userId") int userId, @Param("contentId") int contentId);

    /**
     * ✅ 진도율 신규 등록
     */
    @Insert("""
        INSERT INTO progress_logs (user_id, content_id, progress_percent, last_accessed_at, created_at, updated_at)
        VALUES (#{userId}, #{contentId}, #{progressPercent}, NOW(), NOW(), NOW())
    """)
    void insertProgress(@Param("userId") int userId,
                        @Param("contentId") int contentId,
                        @Param("progressPercent") int progressPercent);

    /**
     * ✅ 진도율 업데이트
     */
    @Update("""
        UPDATE progress_logs
        SET progress_percent = #{progressPercent},
            last_accessed_at = NOW(),
            updated_at = NOW()
        WHERE user_id = #{userId}
          AND content_id = #{contentId}
    """)
    void updateProgress(@Param("userId") int userId,
                        @Param("contentId") int contentId,
                        @Param("progressPercent") int progressPercent);
}

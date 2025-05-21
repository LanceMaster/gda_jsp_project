package model.mapper;

import model.dto.ContentDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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
    
    @Mapper

        // 미완료 콘텐츠 수 확인
        @Select("""
            SELECT COUNT(*) 
            FROM lecture_contents c
            LEFT JOIN progress_logs p 
              ON c.content_id = p.content_id AND p.user_id = #{userId}
            WHERE c.lecture_id = #{lectureId}
              AND (p.progress_percent IS NULL OR p.progress_percent < 100)
        """)
        int countIncompleteContents(@Param("lectureId") int lectureId, @Param("userId") int userId);

        // 진도율 저장 또는 업데이트
        @Insert("""
            INSERT INTO progress_logs (user_id, content_id, progress_percent, updated_at)
            VALUES (#{userId}, #{contentId}, #{progressPercent}, NOW())
            ON DUPLICATE KEY UPDATE 
              progress_percent = #{progressPercent},
              updated_at = NOW()
        """)
        void saveOrUpdateProgress(@Param("userId") int userId,
                                  @Param("contentId") int contentId,
                                  @Param("progressPercent") int progressPercent);
        
        @Insert("""
        	    INSERT INTO progress_logs (user_id, content_id, progress_percent, created_at, updated_at, last_accessed_at)
        	    VALUES (#{userId}, #{contentId}, #{percent}, NOW(), NOW(), NOW())
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

        
        @Update("""
        	    UPDATE enrollments
        	    SET status = 'COMPLETED'
        	    WHERE user_id = #{userId}
        	      AND lecture_id = #{lectureId}
        	      AND status != 'COMPLETED'
        	""")
        	void markEnrollmentComplete(@Param("userId") int userId,
        	                            @Param("lectureId") int lectureId);

        @Select("""
        	    SELECT lecture_id
        	    FROM contents
        	    WHERE content_id = #{contentId}
        	""")
        	Integer selectLectureIdByContentId(@Param("contentId") int contentId);

        
     // mapper/ProgressMapper.java
        @Select("""
            SELECT c.content_id, IFNULL(p.progress_percent, 0) AS progress
            FROM contents c
            LEFT JOIN progress_logs p 
              ON c.content_id = p.content_id AND p.user_id = #{userId}
            WHERE c.lecture_id = #{lectureId}
        """)
        @MapKey("content_id")
        @Results({
            @Result(property = "content_id", column = "content_id"),
            @Result(property = "progress", column = "progress")
        })
        Map<Integer, Integer> selectProgressMap(@Param("userId") int userId,
                                                @Param("lectureId") int lectureId);

        
        }






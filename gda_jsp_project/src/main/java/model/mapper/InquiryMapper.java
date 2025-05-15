package model.mapper;

import model.dto.InquiryDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 📌 InquiryMapper
 * - 강의 문의 관련 SQL 정의
 */
public interface InquiryMapper {

    /** ✅ 문의 전체 목록 (강의명 포함) */
    @Select("""
        SELECT i.inquiry_id AS inquiryId,
               i.lecture_id AS lectureId,
               i.user_id AS userId,
               i.title,
               i.content,
               i.created_at AS createdAt,
               i.updated_at AS updatedAt,
               i.status,
               l.title AS lectureTitle,
               u.name AS userName
        FROM inquiries i
        JOIN lectures l ON i.lecture_id = l.lecture_id
        JOIN users u ON i.user_id = u.user_id
        ORDER BY i.created_at DESC
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<InquiryDTO> getAllInquiries(@Param("limit") int limit, @Param("offset") int offset);

    /** ✅ 총 게시물 수 (페이징용) */
    @Select("SELECT COUNT(*) FROM inquiries")
    int getInquiryCount();

    /** ✅ 단건 문의 조회 */
    @Select("""
        SELECT * FROM inquiries
        WHERE inquiry_id = #{inquiryId}
    """)
    InquiryDTO getInquiryById(@Param("inquiryId") int inquiryId);

    /** ✅ 문의 등록 */
    @Insert("""
        INSERT INTO inquiries (lecture_id, user_id, title, content, created_at, updated_at, status)
        VALUES (#{lectureId}, #{userId}, #{title}, #{content}, NOW(), NOW(), 'OPEN')
    """)
    @Options(useGeneratedKeys = true, keyProperty = "inquiryId")
    void insertInquiry(InquiryDTO dto);

    /** ✅ 문의 수정 */
    @Update("""
        UPDATE inquiries
        SET title = #{title}, content = #{content}, updated_at = NOW()
        WHERE inquiry_id = #{inquiryId}
    """)
    void updateInquiry(InquiryDTO dto);

    /** ✅ 문의 삭제 */
    @Delete("DELETE FROM inquiries WHERE inquiry_id = #{inquiryId}")
    void deleteInquiry(@Param("inquiryId") int inquiryId);
}

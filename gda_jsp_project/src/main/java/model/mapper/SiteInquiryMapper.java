package model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import model.dto.SiteInquiryDTO;

@Mapper
public interface SiteInquiryMapper {

    // ✅ 전체 SITE 문의 목록 (최신순)
    @Select("SELECT i.*, u.name AS userName " +
            "FROM inquiries i " +      // <-- 수정됨
            "JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.type = 'SITE' " +
            "ORDER BY i.created_at DESC")
    List<SiteInquiryDTO> listAll();

    // ✅ 단일 SITE 문의 상세 (작성자 이름 포함)
    @Select("SELECT i.*, u.name AS userName " +
            "FROM inquiries i " +      // <-- 수정됨
            "JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.type = 'SITE' AND i.inquiry_id = #{inquiryId}")
    SiteInquiryDTO findById(@Param("inquiryId") int inquiryId);

    // ✅ SITE 문의 등록
    @Insert("INSERT INTO inquiries (user_id, type, title, content, is_answered) " +  // <-- 수정됨
            "VALUES (#{userId}, 'SITE', #{title}, #{content}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "inquiryId")
    int insert(SiteInquiryDTO inquiry);

    // ✅ SITE 문의 수정
    @Update("UPDATE inquiries SET title = #{title}, content = #{content} " +  // <-- 수정됨
            "WHERE inquiry_id = #{inquiryId} AND type = 'SITE'")
    int update(SiteInquiryDTO inquiry);

    // ✅ SITE 문의 삭제
    @Delete("DELETE FROM inquiries WHERE inquiry_id = #{inquiryId} AND type = 'SITE'")  // <-- 수정됨
    int delete(int inquiryId);

    // ✅ 답변여부 수정(관리자용)
    @Update("UPDATE inquiries SET is_answered = #{isAnswered} WHERE inquiry_id = #{inquiryId} AND type = 'SITE'")  // <-- 수정됨
    int updateIsAnswered(@Param("inquiryId") int inquiryId, @Param("isAnswered") boolean isAnswered);

    // ✅ 제목/내용 키워드 검색 (최신순)
    @Select("SELECT i.*, u.name AS userName " +
            "FROM inquiries i " +      // <-- 수정됨
            "JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.type = 'SITE' AND (i.title LIKE CONCAT('%',#{keyword},'%') OR i.content LIKE CONCAT('%',#{keyword},'%')) " +
            "ORDER BY i.created_at DESC")
    List<SiteInquiryDTO> searchByTitleOrContent(@Param("keyword") String keyword);
    
    // 미답변 문의만 오래된 순으로 조회 (관리자용)
    @Select("SELECT i.*, u.name AS userName " +
            "FROM inquiries i " +
            "JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.type = 'SITE' AND i.is_answered = 0 " +  // 미답변만
            "ORDER BY i.created_at ASC")  // 오래된 순 정렬
    List<SiteInquiryDTO> listUnansweredOrderByOldest();

}

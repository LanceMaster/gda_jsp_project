package model.mapper;

import model.dto.InquiryDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface InquiryMapper {

    @Select("""
        SELECT i.*, l.title AS lectureTitle
        FROM inquiries i
        LEFT JOIN lectures l ON i.lecture_id = l.lecture_id
        ORDER BY i.created_at DESC
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<InquiryDTO> getPagedInquiries(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM inquiries")
    int countInquiries();

    @Select("""
        SELECT i.*, l.title AS lectureTitle
        FROM inquiries i
        LEFT JOIN lectures l ON i.lecture_id = l.lecture_id
        WHERE i.inquiry_id = #{inquiryId}
    """)
    InquiryDTO getInquiryById(@Param("inquiryId") int inquiryId);

    @Delete("DELETE FROM inquiries WHERE inquiry_id = #{inquiryId}")
    int deleteInquiry(@Param("inquiryId") int inquiryId);
    
    @Insert("""
    	    INSERT INTO inquiries (
    	      user_id, type, lecture_id, title, content, is_answered, created_at
    	    ) VALUES (
    	      #{userId}, #{type}, #{lectureId}, #{title}, #{content}, false, #{createdAt}
    	    )
    	""")
    	@Options(useGeneratedKeys = true, keyProperty = "inquiryId")
    	void insertInquiry(InquiryDTO inquiry);

    @Select("SELECT i.*, l.title AS lectureTitle, u.name AS userName " +
            "FROM inquiries i " +
            "LEFT JOIN lectures l ON i.lecture_id = l.lecture_id " +
            "LEFT JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.inquiry_id = #{inquiryId}")
    @Results({
        @Result(property = "inquiryId", column = "inquiry_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "lectureId", column = "lecture_id"),
        @Result(property = "title", column = "title"),
        @Result(property = "content", column = "content"),
        @Result(property = "type", column = "type"),
        @Result(property = "isAnswered", column = "is_answered"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "lectureTitle", column = "lectureTitle"),
        @Result(property = "userName", column = "userName")
    })
    InquiryDTO selectInquiryById(int inquiryId);
}



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
    List<InquiryDTO> selectAll(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM inquiries")
    int countAll();

    @Delete("DELETE FROM inquiries WHERE inquiry_id = #{id}")
    void deleteById(@Param("id") int id);
}

package model.mapper;

import model.dto.InquiryCommentDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface InquiryCommentMapper {

    @Insert("INSERT INTO inquiry_comment (content, is_deleted, inquiry_id, user_id) " +
            "VALUES (#{content}, 0, #{inquiry_id}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "comment_id")
    int insert(InquiryCommentDTO comment);

    @Select("SELECT c.comment_id, c.content, c.is_deleted, c.created_at, c.inquiry_id, c.user_id, u.name AS userName " +
            "FROM inquiry_comment c " +
            "JOIN users u ON c.user_id = u.user_id " +
            "WHERE c.inquiry_id = #{inquiry_id} AND c.is_deleted = 0 " +
            "ORDER BY c.created_at ASC")
    List<InquiryCommentDTO> getCommentsByInquiryId(int inquiry_id);

    @Update("UPDATE inquiry_comment SET is_deleted = 1 WHERE comment_id = #{comment_id}")
    int softDelete(int comment_id);

    // 남은 댓글 수 조회
    @Select("SELECT COUNT(*) FROM inquiry_comment WHERE inquiry_id = #{inquiry_id} AND is_deleted = 0")
    int countActiveCommentsByInquiryId(@Param("inquiry_id") int inquiry_id);
}

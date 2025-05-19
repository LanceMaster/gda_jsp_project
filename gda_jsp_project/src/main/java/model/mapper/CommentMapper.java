package model.mapper;

import model.dto.CommentDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO project_comments (content, parent_id, is_deleted, project_id, user_id) " +
            "VALUES (#{content}, #{parent_id}, 0, #{project_id}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "comment_id")
    int insert(CommentDTO comment);

    // ✅ 작성자 이름 조회를 포함한 댓글 목록 조회
    @Select("SELECT c.comment_id, c.content, c.parent_id, c.is_deleted, c.created_at, c.project_id, c.user_id, u.name AS userName " +
            "FROM project_comments c " +
            "JOIN users u ON c.user_id = u.user_id " +
            "WHERE c.project_id = #{project_id} AND c.is_deleted = 0 " +
            "ORDER BY c.created_at ASC")
    List<CommentDTO> getCommentsByProjectId(int project_id);

    @Update("UPDATE project_comments SET is_deleted = 1 WHERE comment_id = #{comment_id}")
    int softDelete(int comment_id);
}

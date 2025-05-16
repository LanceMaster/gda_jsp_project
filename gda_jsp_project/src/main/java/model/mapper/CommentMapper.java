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

    @Select("SELECT * FROM project_comments WHERE project_id = #{project_id} AND is_deleted = 0 ORDER BY created_at ASC")
    List<CommentDTO> getCommentsByProjectId(int project_id);

    @Update("UPDATE project_comments SET is_deleted = 1 WHERE comment_id = #{comment_id}")
    int softDelete(int comment_id);
}

package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.dto.CommentDTO;
import model.mapper.CommentMapper;
import java.util.List;

public class CommentDAO {
    private final CommentMapper mapper;

    public CommentDAO(SqlSession session) {
        this.mapper = session.getMapper(CommentMapper.class);
    }

    public int insert(CommentDTO comment) {
        return mapper.insert(comment);
    }

    public List<CommentDTO> getCommentsByProjectId(int project_id) {
        return mapper.getCommentsByProjectId(project_id);
    }

    public int softDelete(int comment_id) {
        return mapper.softDelete(comment_id);
    }
}

package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.dto.InquiryCommentDTO;
import model.mapper.InquiryCommentMapper;
import java.util.List;

public class InquiryCommentDAO {
    private final InquiryCommentMapper mapper;

    public InquiryCommentDAO(SqlSession session) {
        this.mapper = session.getMapper(InquiryCommentMapper.class);
    }

    // 댓글 등록
    public int insert(InquiryCommentDTO comment) {
        return mapper.insert(comment);
    }

    // 특정 문의글의 댓글 목록
    public List<InquiryCommentDTO> getCommentsByInquiryId(int inquiryId) {
        return mapper.getCommentsByInquiryId(inquiryId);
    }

    // 댓글 soft delete
    public int softDelete(int commentId) {
        return mapper.softDelete(commentId);
    }

    // 특정 문의글의 삭제되지 않은 댓글 수 조회
    public int countActiveCommentsByInquiryId(int inquiryId) {
        return mapper.countActiveCommentsByInquiryId(inquiryId);
    }
}

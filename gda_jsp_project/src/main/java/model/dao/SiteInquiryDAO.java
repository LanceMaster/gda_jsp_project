package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.dto.SiteInquiryDTO;
import model.mapper.SiteInquiryMapper;
import java.util.List;

public class SiteInquiryDAO {
    private SiteInquiryMapper mapper;

    public SiteInquiryDAO(SqlSession session) {
        this.mapper = session.getMapper(SiteInquiryMapper.class);
    }

    // 전체 SITE 문의 리스트 (최신순)
    public List<SiteInquiryDTO> listAll() {
        return mapper.listAll();
    }

    // 단일 문의 상세 조회
    public SiteInquiryDTO findById(int inquiryId) {
        return mapper.findById(inquiryId);
    }

    // SITE 문의 등록
    public int insert(SiteInquiryDTO inquiry) {
        return mapper.insert(inquiry);
    }

    // SITE 문의 수정
    public int update(SiteInquiryDTO inquiry) {
        return mapper.update(inquiry);
    }

    // SITE 문의 삭제
    public int delete(int inquiryId) {
        return mapper.delete(inquiryId);
    }

    // 답변여부 변경(관리자)
    public int updateIsAnswered(int inquiryId, boolean isAnswered) {
        return mapper.updateIsAnswered(inquiryId, isAnswered);
    }

    // 제목/내용 키워드 검색 (최신순)
    public List<SiteInquiryDTO> searchByTitleOrContent(String keyword) {
        return mapper.searchByTitleOrContent(keyword);
    }
    
 // 미답변 문의만 오래된 순으로 조회 (관리자용)
    public List<SiteInquiryDTO> listUnansweredOrderByOldest() {
        return mapper.listUnansweredOrderByOldest();
    }
}

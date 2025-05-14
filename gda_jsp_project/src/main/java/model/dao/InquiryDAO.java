package model.dao;

import model.dto.InquiryDTO;
import model.mapper.InquiryMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

/**
 * 📌 InquiryDAO
 * - 강의 문의 관련 DB 접근 로직 처리
 */
public class InquiryDAO {

    /** ✅ 전체 문의 목록 조회 (페이징) */
    public List<InquiryDTO> getAllInquiries(int limit, int offset) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.getAllInquiries(limit, offset);
        }
    }

    /** ✅ 총 문의 개수 반환 */
    public int getInquiryCount() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.getInquiryCount();
        }
    }

    /** ✅ 단건 문의 상세 조회 */
    public InquiryDTO getInquiryById(int inquiryId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.getInquiryById(inquiryId);
        }
    }

    /** ✅ 문의글 등록 */
    public void insertInquiry(InquiryDTO dto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.insertInquiry(dto);
        }
    }

    /** ✅ 문의글 수정 */
    public void updateInquiry(InquiryDTO dto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.updateInquiry(dto);
        }
    }

    /** ✅ 문의글 삭제 */
    public void deleteInquiry(int inquiryId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.deleteInquiry(inquiryId);
        }
    }
}

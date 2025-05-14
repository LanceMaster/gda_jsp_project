package service;

import model.dao.InquiryDAO;
import model.dto.InquiryDTO;

import java.util.List;

/**
 * 📚 InquiryService
 * - 비즈니스 로직을 담당하는 서비스 계층
 */
public class InquiryService {

    private final InquiryDAO inquiryDAO;

    public InquiryService() {
        this.inquiryDAO = new InquiryDAO();
    }

    /** ✅ 전체 문의 조회 (페이징 포함) */
    public List<InquiryDTO> getAllInquiries(int limit, int offset) {
        return inquiryDAO.getAllInquiries(limit, offset);
    }

    /** ✅ 총 문의 수 조회 */
    public int getInquiryCount() {
        return inquiryDAO.getInquiryCount();
    }

    /** ✅ 특정 ID로 문의글 상세 조회 */
    public InquiryDTO getInquiryById(int inquiryId) {
        return inquiryDAO.getInquiryById(inquiryId);
    }

    /** ✅ 문의글 등록 */
    public void registerInquiry(InquiryDTO dto) {
        inquiryDAO.insertInquiry(dto);
    }

    /** ✅ 문의글 수정 */
    public void updateInquiry(InquiryDTO dto) {
        inquiryDAO.updateInquiry(dto);
    }

    /** ✅ 문의글 삭제 */
    public void deleteInquiry(int inquiryId) {
        inquiryDAO.deleteInquiry(inquiryId);
    }
}

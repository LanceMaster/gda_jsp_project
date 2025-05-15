// 📁 service/InquiryService.java
package service;

import model.dao.InquiryDAO;
import model.dto.InquiryDTO;

import java.util.List;

/**
 * 📚 InquiryService
 * - 강의 문의글 관련 비즈니스 로직 처리
 * - Controller와 DAO 사이에서 데이터 흐름 제어
 */
public class InquiryService {

    // ✅ DAO 객체 생성 (현재는 직접 생성하지만 향후 DI 구조로 리팩토링 가능)
    private final InquiryDAO inquiryDAO = new InquiryDAO();

    /**
     * ✅ 페이징된 문의글 목록 조회
     * @param limit  한 페이지에 표시할 글 수
     * @param offset 시작 위치 (limit * (page - 1))
     * @return 페이징된 문의글 리스트
     */
    public List<InquiryDTO> getPagedInquiries(int limit, int offset) {
        return inquiryDAO.selectAll(limit, offset);
    }

    /**
     * ✅ 전체 문의글 수 조회 (페이징 계산용)
     * @return 총 문의글 수
     */
    public int getTotalCount() {
        return inquiryDAO.countAll();
    }

    /**
     * ✅ 특정 문의글 삭제
     * @param inquiryId 삭제할 문의글 ID
     */
    public void deleteInquiry(int inquiryId) {
        inquiryDAO.deleteById(inquiryId);
    }

    // 📌 향후 추가 기능 예시 (예: 등록, 수정, 상세 조회 등)
    // public InquiryDTO getInquiryById(int id) { ... }
    // public void createInquiry(InquiryDTO dto) { ... }
    // public void updateInquiry(InquiryDTO dto) { ... }
}

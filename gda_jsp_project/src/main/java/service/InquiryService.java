package service;

import model.dao.InquiryDAO;
import model.dto.InquiryDTO;

import java.util.List;

public class InquiryService {

    private final InquiryDAO inquiryDAO = new InquiryDAO();

    public List<InquiryDTO> getPagedInquiries(int page, int size) {
        int offset = (page - 1) * size;
        return inquiryDAO.getPagedInquiries(size, offset);
    }

    public int getTotalInquiries() {
        return inquiryDAO.countInquiries();
    }
 // InquiryService.java

    public void insertInquiry(InquiryDTO inquiry) {
        inquiryDAO.insertInquiry(inquiry);
    }

    public InquiryDTO getInquiryById(int inquiryId) {
        return inquiryDAO.getInquiryById(inquiryId);
    }

    public boolean deleteInquiry(int inquiryId) {
        return inquiryDAO.deleteInquiry(inquiryId);
    }
    
    
}

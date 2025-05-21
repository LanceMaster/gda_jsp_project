package model.dao;

import model.dto.InquiryDTO;
import model.mapper.InquiryMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

public class InquiryDAO {

    public List<InquiryDTO> getPagedInquiries(int limit, int offset) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.getPagedInquiries(limit, offset);
        }
    }

    public int countInquiries() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.getMapper(InquiryMapper.class).countInquiries();
        }
    }

    public InquiryDTO getInquiryById(int inquiryId) {
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
        try {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.selectInquiryById(inquiryId);
        } finally {
            session.close();
        }
    }

    
    

    public void insertInquiry(InquiryDTO inquiry) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.insertInquiry(inquiry);
            session.commit();
        }
    }
    
    

    
    public boolean deleteInquiry(int inquiryId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            int rows = session.getMapper(InquiryMapper.class).deleteInquiry(inquiryId);
            session.commit();
            return rows > 0;
        }
    }


	public void updateInquiry(InquiryDTO inquiry) {
		try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.updateInquiry(inquiry);
            session.commit();
            session.close();
		} 
	}
}

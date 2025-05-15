package model.dao;

import model.dto.InquiryDTO;
import model.mapper.InquiryMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

public class InquiryDAO {

    public List<InquiryDTO> selectAll(int limit, int offset) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.selectAll(limit, offset);
        }
    }

    public int countAll() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            return mapper.countAll();
        }
    }

    public void deleteById(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            InquiryMapper mapper = session.getMapper(InquiryMapper.class);
            mapper.deleteById(id);
            session.commit();
        }
    }
}
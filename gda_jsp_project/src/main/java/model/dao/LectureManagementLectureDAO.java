package model.dao;

import model.dto.LectureManagementLectureDTO;
import model.mapper.LectureManagementLectureMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import utils.MyBatisUtil;

import java.util.List;

public class LectureManagementLectureDAO {

    private final SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();

    public List<LectureManagementLectureDTO> getLecturesByInstructorFiltered(
            Long instructorId, String search, String category, String status, String sort) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            return mapper.selectLecturesByInstructorFiltered(instructorId, search, category, status, sort);
        }
    }

    public List<String> getAllCategories() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            return mapper.selectAllCategories();
        }
    }
    
    public boolean updateLectureField(Long lectureId, String field, String value) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            int result = mapper.updateLectureField(lectureId, field, value);
            session.commit();
            return result > 0;
        }
    }
    
    public boolean updateStatus(Long lectureId, String status) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            int result = mapper.updateStatus(lectureId, status);
            session.commit();
            return result > 0;
        }
    }
    
    public boolean deleteLecture(Long lectureId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            int result = mapper.deleteLecture(lectureId);
            session.commit();
            return result > 0;
        }
    }

}

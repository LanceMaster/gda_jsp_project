package model.dao;

import model.dto.LectureManagementLectureDTO;
import model.mapper.LectureManagementLectureMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 강의 관리 DAO - MyBatis 매퍼(@Mapper) 직접 호출 방식
 */
public class LectureManagementLectureDAO {

    /**
     * 강사의 강의 목록을 조건에 따라 조회
     */
    public List<LectureManagementLectureDTO> selectLecturesByInstructor(
            int instructorId, String search, String category, String status, String sort
    ) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            Map<String, Object> params = new HashMap<>();
            params.put("instructorId", instructorId);
            params.put("search", search);
            params.put("category", category);
            params.put("status", status);
            params.put("sort", sort);
            return mapper.selectLecturesByInstructor(params);
        }
    }

    /**
     * 강의의 개별 필드를 동적으로 업데이트 (field: 컬럼명, value: 값)
     */
    public int updateLectureField(Long lectureId, String field, String value) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            Map<String, Object> params = new HashMap<>();
            params.put("lectureId", lectureId);
            params.put("field", field);
            params.put("value", value);
            return mapper.updateLectureField(params);
        }
    }

    /**
     * 강의 공개 상태 변경
     */
    public int updateLectureStatus(Long lectureId, String status) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            return mapper.updateLectureStatus(lectureId, status);
        }
    }

    /**
     * 강의 삭제
     */
    public int deleteLectureById(Long lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            return mapper.deleteLectureById(lectureId);
        }
    }

    /**
     * 전체 카테고리 목록 조회
     */
    public List<String> selectAllCategories() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LectureManagementLectureMapper mapper = session.getMapper(LectureManagementLectureMapper.class);
            return mapper.selectAllCategories();
        }
    }
}

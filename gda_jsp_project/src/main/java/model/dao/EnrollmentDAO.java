package model.dao;

import model.mapper.EnrollmentMapper;
import org.apache.ibatis.session.SqlSession;

public class EnrollmentDAO {
    private final SqlSession session;

    public EnrollmentDAO(SqlSession session) {
        this.session = session;
    }

    public boolean isUserEnrolled(int userId, int lectureId) {
        EnrollmentMapper mapper = session.getMapper(EnrollmentMapper.class);
        Integer result = mapper.isUserEnrolled(userId, lectureId);
        return result != null;
    }
}

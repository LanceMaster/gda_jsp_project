package service;

import model.dao.LecturePlayDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

public class LecturePlayService {

    public LectureDTO getLectureDetail(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LecturePlayDAO dao = new LecturePlayDAO(session);
            return dao.selectLectureById(lectureId);
        }
    }

    public List<ContentDTO> getLectureContents(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LecturePlayDAO dao = new LecturePlayDAO(session);
            return dao.selectContentsByLectureId(lectureId);
        }
    }
}

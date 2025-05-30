package service;

import model.dao.TagDAO;
import model.dto.TagDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

public class TagService {

    /**
     * ✅ 전체 태그 목록 조회
     */
    public List<TagDTO> getAllTags() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            TagDAO tagDAO = new TagDAO(session);
            return tagDAO.getAllTags();
        }
    }

    /**
     * ✅ 특정 강의의 태그 목록 조회
     * - lectureId에 연결된 태그들을 반환
     */
    public List<TagDTO> getTagsByLectureId(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            TagDAO tagDAO = new TagDAO(session);
            return tagDAO.getTagsByLectureId(lectureId);
        }
    }
}

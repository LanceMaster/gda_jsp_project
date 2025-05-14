package model.dao;

import model.dto.TagDTO;
import model.mapper.TagMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

/**
 * 🏷️ TagDAO
 * - 강의 태그 데이터 접근 객체
 */
public class TagDAO {

    /**
     * ✅ 강의 ID로 연결된 태그 목록 조회
     */
    public List<TagDTO> getTagsByLectureId(int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            return mapper.getTagsByLectureId(lectureId);
        }
    }
}

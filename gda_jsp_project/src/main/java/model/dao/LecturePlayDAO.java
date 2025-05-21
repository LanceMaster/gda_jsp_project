// ✅ 3. LecturePlayDAO.java
package model.dao;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.mapper.LecturePlayMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class LecturePlayDAO {
    private final SqlSession session;
    public LecturePlayDAO(SqlSession session) { this.session = session; }

    public LectureDTO selectLectureById(int lectureId) {
        return session.getMapper(LecturePlayMapper.class).selectLectureById(lectureId);
    }
    
    public List<ContentDTO> selectContentsWithProgress(int lectureId, int userId) {
        LecturePlayMapper mapper = session.getMapper(LecturePlayMapper.class);
        return mapper.selectContentsWithProgress(lectureId, userId);
    }

    public List<ContentDTO> selectContentsByLectureId(int lectureId) {
        return session.getMapper(LecturePlayMapper.class).selectContentsByLectureId(lectureId);
    }
}
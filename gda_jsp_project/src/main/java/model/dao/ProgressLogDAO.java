

// ✅ ProgressLogDAO.java
package model.dao;

import model.dto.ContentDTO;
import model.mapper.LectureMapper;
import model.mapper.ProgressMapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class ProgressLogDAO {
    private final SqlSession session;
    public ProgressLogDAO(SqlSession session) {
        this.session = session;
    }

    public void upsertProgress(int userId, int contentId, int percent) {
        session.getMapper(ProgressMapper.class).upsertProgress(userId, contentId, percent);
    }
    
    public List<ContentDTO> getAllContentsByLectureId(int lectureId) {
        return session.getMapper(LectureMapper.class).selectAllContents(lectureId);
    }
    
    public void saveOrUpdateProgress(int userId, int contentId, int progressPercent) {
        session.getMapper(ProgressMapper.class)
               .saveOrUpdateProgress(userId, contentId, progressPercent);
    }

    public boolean checkLectureCompletion(int lectureId, int userId) {
        int incomplete = session.getMapper(ProgressMapper.class)
                                .countIncompleteContents(lectureId, userId);
        return incomplete == 0;
    }
}
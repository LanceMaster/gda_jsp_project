package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.mapper.ProgressMapper;
import utils.MybatisConnection;

public class ProgressDAO {

    public void saveOrUpdateProgress(int userId, int contentId, int progressPercent) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            ProgressMapper mapper = session.getMapper(ProgressMapper.class);
            Integer existing = mapper.checkExist(userId, contentId);

            if (existing != null) {
                mapper.updateProgress(userId, contentId, progressPercent);
            } else {
                mapper.insertProgress(userId, contentId, progressPercent);
            }

            session.commit();
        }
    }
}

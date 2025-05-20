package service;

import model.mapper.ProgressMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

/**
 * 📚 ProgressService
 * - 사용자의 콘텐츠 진도율 저장/갱신 서비스
 */
public class ProgressService {

    /**
     * ✅ 진도율 저장 or 갱신
     * - 기존 기록이 있으면 update
     * - 없으면 insert
     */
    public void saveOrUpdateProgress(int userId, int contentId, int progressPercent) {
        SqlSession session = null;

        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(true); // 자동 커밋
            ProgressMapper mapper = session.getMapper(ProgressMapper.class);

            Integer progressId = mapper.checkExist(userId, contentId);

            if (progressId == null) {
                // ⏺️ 신규 진도율 기록
                mapper.insertProgress(userId, contentId, progressPercent);
            } else {
                // 🔄 기존 진도율 갱신
                mapper.updateProgress(userId, contentId, progressPercent);
            }

        } catch (Exception e) {
            throw new RuntimeException("⚠️ 진도율 저장/갱신 중 오류 발생", e);
        } finally {
            if (session != null) session.close();
        }
    }
}

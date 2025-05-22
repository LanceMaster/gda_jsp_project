package service;

import model.mapper.ProgressMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.Map;

/**
 * 📚 ProgressService
 * - 사용자의 콘텐츠 진도율 저장 및 수료 상태 처리 서비스
 */
public class ProgressService {

    /**
     * ✅ 진도율 저장 또는 갱신
     * - upsertProgress(): 진도율이 높을 때만 저장되도록 SQL에서 제어
     * - 모든 진도율 처리는 이 메서드 하나로 일원화
     */
    public void saveOrUpdateProgress(int userId, int contentId, int progressPercent) {
        SqlSession session = null;
        try {
            session = MyBatisUtil.getSqlSessionFactory().openSession(true); // auto commit
            ProgressMapper mapper = session.getMapper(ProgressMapper.class);

            // ✅ 1. 진도율 저장 or 갱신
            mapper.upsertProgress(userId, contentId, progressPercent);

            // ✅ 2. 강의 ID 가져오기
            Integer lectureId = mapper.selectLectureIdByContentId(contentId);
            if (lectureId != null) {

                // ✅ 3. 완료하지 않은 콘텐츠 개수 조회
                int remaining = mapper.countIncompleteContents(lectureId, userId);

                // ✅ 4. 모든 콘텐츠 완료 시 수강 상태 완료 처리
                if (remaining == 0) {
                    mapper.markEnrollmentComplete(userId, lectureId);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("❌ 진도율 저장 또는 수료 상태 처리 중 오류", e);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * ✅ 콘텐츠별 진도율 조회 (강의 상세 화면에서 사용)
     * - Map<contentId, progressPercent> 형태 반환
     */
    public Map<Integer, Integer> getProgressMap(int userId, int lectureId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            ProgressMapper mapper = session.getMapper(ProgressMapper.class);
            return mapper.selectProgressMap(userId, lectureId);
        }
    }
}

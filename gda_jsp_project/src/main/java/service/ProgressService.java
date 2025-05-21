package service;

import model.mapper.ProgressMapper;

import java.util.Map;

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
	        session = MyBatisUtil.getSqlSessionFactory().openSession(true); // auto commit
	        ProgressMapper mapper = session.getMapper(ProgressMapper.class);

	        // ✅ 진도율 저장/업데이트
	        mapper.upsertProgress(userId, contentId, progressPercent);

	        // ✅ 해당 content의 강의 ID 가져오기
	        Integer lectureId = mapper.selectLectureIdByContentId(contentId); // 아래에 정의됨

	        if (lectureId != null) {
	            // ✅ 해당 강의에서 완료하지 않은 콘텐츠 개수 확인
	            int remaining = mapper.countIncompleteContents(lectureId, userId);

	            // ✅ 수료 조건 충족 시 enrollments 상태 업데이트
	            if (remaining == 0) {
	                mapper.markEnrollmentComplete(userId, lectureId);
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("진도율 저장 또는 수료 상태 처리 중 오류", e);
	    } finally {
	        if (session != null) session.close();
	    }
	}
	
	// service/ProgressService.java
	public Map<Integer, Integer> getProgressMap(int userId, int lectureId) {
	    try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
	        ProgressMapper mapper = session.getMapper(ProgressMapper.class);
	        return mapper.selectProgressMap(userId, lectureId); // contentId -> percent
	    }
	}


}

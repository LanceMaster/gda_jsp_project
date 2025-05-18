package service;

import model.dao.LectureUploadDAO;

import model.dao.TagDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

public class LectureUploadService {
	 /**
     * 🔹 강의 + 콘텐츠 + 태그 등록 (트랜잭션 포함)
     * - 파일은 이미 저장된 상태 (Controller에서 처리)
     * - DB 입력 시 오류 발생하면 전체 롤백
     */
    public boolean registerLectureWithContentAndTags(LectureDTO lecture, ContentDTO content, String[] tagIds) {
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false);  // 수동 커밋

        try {
        	LectureUploadDAO lectureDAO = new LectureUploadDAO(session);
            TagDAO tagDAO = new TagDAO(session);

            // 1️⃣ 강의 등록
            int lectureId = lectureDAO.insertLecture(lecture); // lectureId 자동 생성됨
            content.setLectureId(lectureId); // FK 설정

            // 2️⃣ 콘텐츠 등록
            lectureDAO.insertContent(content);

            // 3️⃣ 태그 매핑 등록
            if (tagIds != null) {
                for (String tagIdStr : tagIds) {
                    int tagId = Integer.parseInt(tagIdStr);
                    tagDAO.insertMapping(lectureId, "LECTURE", tagId);
                }
            }

            session.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            return false;

        } finally {
            session.close();
        }
    }
}
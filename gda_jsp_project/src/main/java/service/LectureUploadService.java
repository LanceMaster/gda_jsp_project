package service;

import model.dao.LectureUploadDAO;
import model.dao.TagDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;

public class LectureUploadService {

    /**
     * ✅ 다중 콘텐츠와 태그를 포함한 강의 등록
     */
    public boolean registerLectureWithContentsAndTags(LectureDTO lecture, List<ContentDTO> contents, String[] tagIds) {
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false); // 수동 commit
        try {
            LectureUploadDAO lectureDAO = new LectureUploadDAO(session);
            TagDAO tagDAO = new TagDAO(session);

            // 1️⃣ 강의 등록
            int lectureId = lectureDAO.insertLecture(lecture);

            // 2️⃣ 콘텐츠들 등록
            for (ContentDTO content : contents) {
                content.setLectureId(lectureId);
                lectureDAO.insertContent(content);
            }

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

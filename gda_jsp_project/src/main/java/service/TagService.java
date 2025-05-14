package service;

import model.dao.TagDAO;
import model.dto.TagDTO;

import java.util.List;

/**
 * 🏷️ TagService
 * - 강의 태그 조회 비즈니스 로직
 */
public class TagService {

    private final TagDAO tagDAO = new TagDAO();

    /**
     * ✅ 특정 강의에 연결된 태그 목록 조회
     */
    public List<TagDTO> getTagsByLectureId(int lectureId) {
        return tagDAO.getTagsByLectureId(lectureId);
    }
}

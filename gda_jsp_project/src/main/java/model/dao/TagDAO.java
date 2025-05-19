package model.dao;

import model.dto.TagDTO;
import model.mapper.TagMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class TagDAO {
    private final TagMapper mapper;

    // ✅ 세션 주입 생성자
    public TagDAO(SqlSession session) {
        this.mapper = session.getMapper(TagMapper.class);
    }

    // ✅ 특정 강의에 연결된 태그 목록 조회
    public List<TagDTO> getTagsByLectureId(int lectureId) {
        return mapper.getTagsByLectureId(lectureId);
    }

    // ✅ 특정 프로젝트에 연결된 태그 목록 조회
    public List<TagDTO> getTagsByProjectId(int projectId) {
        return mapper.getTagsByProjectId(projectId);
    }

    // ✅ 전체 태그 목록 조회
    public List<TagDTO> getAllTags() {
        return mapper.getAllTags();
    }

    // ✅ 태그 매핑 추가
    public void insertMapping(int targetId, String targetType, int tagId) {
        mapper.insertMapping(targetId, targetType, tagId);
    }

    // ✅ 태그 매핑 삭제 (targetId + targetType 기반 삭제)
    public void deleteMappings(int targetId, String targetType) {
        mapper.deleteMappings(targetId, targetType);
    }
}

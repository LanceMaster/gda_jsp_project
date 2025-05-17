package model.dao;

import model.dto.TagDTO;
import model.mapper.TagMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class TagDAO {
	private final SqlSession session; // ✅ 세션 주입을 위한 필드 선언

	// ✅ 세션 주입 생성자
	public TagDAO(SqlSession session) {
		this.session = session;
	}

	// ✅ 세션 주입 생성자
	public TagDAO() {
		this.session = null;
	}

	public List<TagDTO> getTagsByLectureId(int lectureId) {
		TagMapper mapper = session.getMapper(TagMapper.class);
		return mapper.getTagsByLectureId(lectureId);
	}


    public void insertMapping(int targetId, String targetType, int tagId) {
        session.getMapper(TagMapper.class).insertMapping(targetId, targetType, tagId);
    }
	/**
	 * ✅ 특정 프로젝트에 연결된 태그 목록 조회
	 */
	public List<TagDTO> getTagsByProjectId(int projectId) {
		TagMapper mapper = session.getMapper(TagMapper.class);
		return mapper.getTagsByProjectId(projectId);
	}

	/**
	 * ✅ 전체 태그 목록 조회
	 */
	public List<TagDTO> getAllTags() {
		TagMapper mapper = session.getMapper(TagMapper.class);
		return mapper.getAllTags();
	}

	/**
	 * ✅ 태그 매핑 추가
	 */
//	public void insertMapping(int targetId, String targetType, int tagId) {
//		TagMapper mapper = session.getMapper(TagMapper.class);
//		mapper.insertMapping(targetId, targetType, tagId);
//	}
	

	    public List<TagDTO> getTopTags(int limit) {
	        TagMapper mapper = session.getMapper(TagMapper.class);
	        return mapper.getTopTags(limit);
	    }
}

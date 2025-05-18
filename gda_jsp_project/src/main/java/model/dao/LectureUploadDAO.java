package model.dao;

import model.dto.ContentDTO;

import model.dto.LectureDTO;
import model.mapper.LectureUploadMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

/**
 * 🎓 LectureDAO
 * - 강의, 콘텐츠 관련 데이터 처리
 * - MyBatis Mapper 인터페이스 기반
 * - Service로부터 세션을 주입받아 트랜잭션 처리 가능
 */
public class LectureUploadDAO {

    private final SqlSession session;

    // 🔹 생성자: 트랜잭션 처리 위해 Session 주입 필수
    public LectureUploadDAO(SqlSession session) {
        this.session = session;
    }

    /**
     * ✅ 강의 등록
     * - @Options 설정으로 insert 후 lectureId 자동 세팅됨
     */
    public int insertLecture(LectureDTO lectureDTO) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        mapper.insertLecture(lectureDTO);
        return lectureDTO.getLectureId();  // auto-generated key
    }

    /**
     * ✅ 콘텐츠 등록
     */
    public void insertContent(ContentDTO contentDTO) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        mapper.insertContent(contentDTO);
    }

    /**
     * ✅ 첫 번째 콘텐츠 조회 (재생용)
     */
    public ContentDTO getFirstContentByLectureId(int lectureId) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        return mapper.getFirstContentByLectureId(lectureId);
    }

    /**
     * ✅ 강의 상세 조회
     */
    public LectureDTO getLectureById(int lectureId) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        return mapper.getLectureById(lectureId);
    }

    /**
     * ✅ 동적 조건 기반 목록 조회
     */
    public List<LectureDTO> getLecturesFilteredSorted(Map<String, Object> params) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        return mapper.getLecturesFilteredSorted(params);
    }

    /**
     * ✅ 강의 평균 평점 갱신
     */
    public void updateLectureRating(int lectureId) {
    	LectureUploadMapper mapper = session.getMapper(LectureUploadMapper.class);
        mapper.updateLectureRating(lectureId);
    }
}

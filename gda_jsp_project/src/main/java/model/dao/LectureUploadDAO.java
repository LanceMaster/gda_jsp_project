package model.dao;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.mapper.LectureUploadMapper;
import org.apache.ibatis.session.SqlSession;

public class LectureUploadDAO {
    private final SqlSession session;

    public LectureUploadDAO(SqlSession session) {
        this.session = session;
    }

    public int insertLecture(LectureDTO dto) {
        session.getMapper(LectureUploadMapper.class).insertLecture(dto);
        return dto.getLectureId();  // 자동 생성 PK 반환
    }

    public void insertContent(ContentDTO contentDTO) {
        session.getMapper(LectureUploadMapper.class).insertContent(contentDTO);
    }
}

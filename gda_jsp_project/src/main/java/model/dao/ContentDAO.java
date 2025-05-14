package model.dao;

import model.dto.ContentDTO;
import model.mapper.LectureMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

/**
 * 🎬 ContentDAO
 * - 강의 콘텐츠(lecture_contents) 등록 및 향후 확장 기능 관리
 * - MyBatis Mapper 기반 DAO
 */
public class ContentDAO {

    // ✅ 트랜잭션 관리를 위한 외부 세션 주입 가능
    private SqlSession session;

    /** ✅ 기본 생성자 (내부 세션 자동 처리용) */
    public ContentDAO() {}

    /** ✅ 세션 주입형 생성자 (트랜잭션 외부 처리용) */
    public ContentDAO(SqlSession session) {
        this.session = session;
    }

    /**
     * ✅ 콘텐츠 등록 메서드
     * - 강의 콘텐츠를 DB에 저장
     * - 외부 세션이 존재하면 해당 세션 사용, 없으면 내부에서 자동 생성 및 종료
     * - Mapper 방식 사용
     */
    public void insertContent(ContentDTO contentDTO) {
        // 세션 분기 처리
        SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession(true);
        try {
            // Mapper 호출
            LectureMapper mapper = useSession.getMapper(LectureMapper.class);
            mapper.insertContent(contentDTO);
        } finally {
            // 외부 세션이 없을 경우만 세션 종료 (트랜잭션 커밋 포함)
            if (session == null) useSession.close();
        }
    }

    // 📌 추후 확장 예정
    // public List<ContentDTO> getContentsByLectureId(int lectureId) { ... }
    // public int updateContent(ContentDTO contentDTO) { ... }
    // public int deleteContent(int contentId) { ... }
}

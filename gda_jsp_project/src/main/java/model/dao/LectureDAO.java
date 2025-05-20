package model.dao;

import model.dto.ContentDTO;
import model.dto.LectureCardDTO;
import model.dto.LectureDTO;
import model.dto.LectureSearchCondition;
import model.mapper.LectureMapper;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;
import java.util.Map;

/**
 * 🎓 LectureDAO - 강의 및 콘텐츠 관련 DAO - MyBatis Mapper 기반 (인터페이스 방식)
 */
public class LectureDAO {

	private SqlSession session;

	/** 기본 생성자 (자동 세션 관리용) */
	public LectureDAO() {
	}

	/** 트랜잭션 처리용 세션 주입 생성자 */
	public LectureDAO(SqlSession session) {
		this.session = session;
	}

	public List<LectureCardDTO> findLectures(LectureSearchCondition cond) {
		LectureMapper mapper = session.getMapper(LectureMapper.class);
		return mapper.findLectures(cond); // ✅ 이게 핵심
	}

	public int countLectures(LectureSearchCondition cond) {
		LectureMapper mapper = session.getMapper(LectureMapper.class);
		return mapper.countLectures(cond);
	}

	// ✅ 1. 전체 강의 목록 조회
	public List<LectureDTO> getAllLectures() {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getAllLectures();
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	// ✅ 2. 카테고리별 강의 조회
	public List<LectureDTO> getLecturesByCategory(String category) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.selectByCategory(category);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	// ✅ 3. 키워드 검색 (title, description)
	public List<LectureDTO> searchLecturesByKeyword(String keyword) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.searchByKeyword(keyword);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	public List<LectureDTO> getLectureList(String keyword, String category, String sort) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.searchLectureList(keyword, category, sort);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	public List<LectureDTO> selectLectures(String keyword, String category, String sort) {
		try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
			LectureMapper mapper = session.getMapper(LectureMapper.class);
			return mapper.selectLectures(keyword, category, sort);
		}
	}

	// ✅ 4. 강의 상세 조회
	public LectureDTO getLectureById(int lectureId) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getLectureById(lectureId);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	// ✅ 5. 첫 번째 콘텐츠 조회 (order_no = 1)
	public ContentDTO getFirstContentByLectureId(int lectureId) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getFirstContentByLectureId(lectureId);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	public String selectTitleById(int lectureId) {
		try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
			LectureMapper mapper = session.getMapper(LectureMapper.class);
			return mapper.selectTitleById(lectureId);
		}
	}

//    // ✅ 6. 강의 등록 (세션 기반 트랜잭션)
//    public int insertLecture(LectureDTO lectureDTO) {
//        SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession(true);
//        try {
//            LectureMapper mapper = useSession.getMapper(LectureMapper.class);
//            mapper.insertLecture(lectureDTO); // @Options로 lectureId 자동 주입
//            return lectureDTO.getLectureId();
//        } finally {
//            if (session == null) useSession.close();
//        }
//    }

	// ✅ 7. 콘텐츠 등록
//    public void insertContent(ContentDTO contentDTO) {
//        SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession(true);
//        try {
//            LectureMapper mapper = useSession.getMapper(LectureMapper.class);
//            mapper.insertContent(contentDTO);
//        } finally {
//            if (session == null) useSession.close();
//        }
//    }

	// ✅ 8. 강의 평점 평균 갱신
	public void updateLectureRating(int lectureId) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession(true);
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			mapper.updateLectureRating(lectureId);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	public List<LectureDTO> getLecturesFilteredSorted(Map<String, Object> params) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = session.getMapper(LectureMapper.class);
			return mapper.getLecturesFilteredSorted(params);
		} finally {
			session.close();
		}
	}

	/**
	 * 김준희 추가 (2025-05-19) 인기 강의 8개 조회
	 * 
	 * @param limit
	 * @return
	 */
	public List<LectureDTO> getTopLectures(int limit) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getTopLectures(limit);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	/**
	 * 김준희 추가 (2025-05-19) 최신 강의 8개 조회
	 * 
	 * @param limit
	 * @return
	 */
	public List<LectureDTO> getLatestLectures(int limit) {
		// TODO Auto-generated method stub
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getLatestLectures(limit);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	/**
	 * 김준희 추가 (2025-05-19) 내가 등록한 강의 조회
	 * 
	 * @param userId
	 * @return
	 */
	public List<LectureDTO> getMyLectures(int userId) {

		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getMyLectures(userId);
		} finally {
			if (session == null)
				useSession.close();
		}

	}

	/**
	 * 김준희 추가 (2025-05-19) 내가 수강중인 강의 조회
	 * 
	 * @param userId
	 * @return
	 */
	public List<LectureDTO> getMyCourses(int userId) {
		// TODO Auto-generated method stub
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.getMyCourses(userId);
		} finally {
			if (session == null)
				useSession.close();
		}
	}

	/**
	 * 김준희 추가
	 * @param userId
	 * @param lectureId
	 * @return
	 */
	public int hasPurchasedLecture(int userId, int lectureId) {
		SqlSession useSession = (session != null) ? session : MyBatisUtil.getSqlSessionFactory().openSession();
		try {
			LectureMapper mapper = useSession.getMapper(LectureMapper.class);
			return mapper.hasPurchasedLecture(userId, lectureId);
		} finally {
			if(session == null)
				useSession.close();
		}

	}

}

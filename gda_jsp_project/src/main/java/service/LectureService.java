package service;

import model.dao.ContentDAO;

import model.dao.LectureDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 📚 LectureService
 * - 강의와 강의 콘텐츠에 대한 서비스 계층 로직 관리
 * - 전체 강의, 카테고리별, 키워드 검색 등 비즈니스 로직 중심
 */
public class LectureService {

    // 기본 DAO - 세션 내부에서 자동 열고 닫는 구조
    private final LectureDAO lectureDAO = new LectureDAO();
    private final ContentDAO contentDAO = new ContentDAO();

    /**
     * ✅ 전체 강의 목록 조회
     * - status = 'PUBLISHED' 조건 포함
     */
    public List<LectureDTO> getAllLectures() {
        return lectureDAO.getAllLectures();
    }
    public List<LectureDTO> getLectureList(String keyword, String category, String sort) {
        return lectureDAO.selectLectures(keyword, category, sort);
    }


    /**
     * ✅ 카테고리로 강의 목록 조회
     * - category 필터 기반
     */
    public List<LectureDTO> getLecturesByCategory(String category) {
        return lectureDAO.getLecturesByCategory(category);
    }

    /**
     * ✅ 키워드로 강의 검색
     * - title, description 기준 LIKE 검색
     */
    public List<LectureDTO> searchLecturesByKeyword(String keyword) {
        // `%`를 포함시켜 LIKE 연산이 되도록 가공
        return lectureDAO.searchLecturesByKeyword("%" + keyword + "%");
    }

    /**
     * ✅ 강의 상세 정보 조회
     */
    public LectureDTO getLectureById(int lectureId) {
        return lectureDAO.getLectureById(lectureId);
    }

    /**
     * ✅ 첫 콘텐츠 조회 (order_no = 1)
     */
    public ContentDTO getFirstContentByLectureId(int lectureId) {
        return lectureDAO.getFirstContentByLectureId(lectureId);
    }

    /**
     * ✅ 강의 평점 평균 갱신
     * - 리뷰 작성/삭제 시 호출됨
     */
    public void updateAverageRating(int lectureId) {
        lectureDAO.updateLectureRating(lectureId);
    }
    

    public List<LectureDTO> getLecturesByCategorySorted(String category, String sort) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("sort", sort);
        return lectureDAO.getLecturesFilteredSorted(params);
    }

    public List<LectureDTO> getAllLecturesSorted(String sort) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", null);
        params.put("sort", sort);
        return lectureDAO.getLecturesFilteredSorted(params);
    }
    
    public String getLectureTitleById(int lectureId) {
        return lectureDAO.selectTitleById(lectureId);
    }
    
    
    
    public List<LectureDTO> searchLectures(String keyword, String category, String sort) {
        // 유사 키워드 매핑 처리
        List<String> keywords = mapRelatedKeywords(keyword);
        return lectureDAO.searchLectures(keywords, category, sort);
    }

    private List<String> mapRelatedKeywords(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;

        keyword = keyword.toLowerCase();
        List<String> result = new ArrayList<>();
        result.add(keyword);

        switch (keyword) {
            case "자바": case "java":
                result.add("java");
                result.add("자바");
                break;
            case "스프링": case "spring":
                result.add("spring");
                result.add("스프링");
                break;
            case "파이썬": case "python":
                result.add("python");
                result.add("파이썬");
                break;
            case "백엔드":
                result.add("backend");
                result.add("백엔드");
                break;
            case "프론트엔드":
                result.add("frontend");
                result.add("프론트엔드");
                break;
            // 필요한 경우 계속 추가 가능
        }

        return result.stream().distinct().toList();
    }



    /**
     * ✅ 강의 등록 + 콘텐츠 등록
     * - MyBatis 수동 커밋을 통한 트랜잭션 처리
     */
    public void registerLectureWithContent(LectureDTO lectureDTO, ContentDTO contentDTO) {
        SqlSession session = null;
        try {
            // 🔒 트랜잭션 시작 (수동 커밋)
            session = MyBatisUtil.getSqlSessionFactory().openSession(false);

            // 세션 기반 DAO 생성
            LectureDAO lectureDAO = new LectureDAO(session);
            ContentDAO contentDAO = new ContentDAO(session);

            // 1. 강의 등록 (PK 생성됨)
            int lectureId = lectureDAO.insertLecture(lectureDTO);

            // 2. FK로 콘텐츠에 강의 ID 연결
            contentDTO.setLectureId(lectureId);

            // 3. 콘텐츠 등록
            contentDAO.insertContent(contentDTO);

            // 4. 커밋
            session.commit();

        } catch (Exception e) {
            if (session != null) session.rollback();
            throw new RuntimeException("⚠️ 강의/콘텐츠 등록 실패: " + e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }
    }
}

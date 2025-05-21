package service;

import model.dao.LectureDAO;
import model.dao.LectureManagementLectureDAO;
import model.dto.LectureManagementLectureDTO;

import java.util.List;

/**
 * 강사 전용 강의 관리 서비스
 * MVC2 아키텍처 기반으로 Controller와 DAO 사이의 중간 계층 역할
 */
public class LectureManagementLectureService {

    private final LectureManagementLectureDAO lectureManagementLectureDAO = new LectureManagementLectureDAO();

    /**
     * 강사의 강의 목록을 검색/필터/정렬 조건에 따라 반환
     * @param instructorId 강사 ID
     * @param search 검색 키워드 (제목, 설명 등)
     * @param category 카테고리 필터
     * @param status 상태 필터 (PUBLISHED, DRAFT)
     * @param sort 정렬 조건 (created_at, price_asc 등)
     * @return 강의 DTO 리스트
     */
    public List<LectureManagementLectureDTO> getLecturesByInstructorFiltered(
            int instructorId, String search, String category, String status, String sort) {
        return lectureManagementLectureDAO.selectLecturesByInstructor(instructorId, search, category, status, sort);
    }

    /**
     * 강의의 개별 필드(제목, 설명, 가격, 카테고리 등)를 수정
     * @param lectureId 강의 ID
     * @param field 수정할 필드명
     * @param value 새로운 값
     * @return 성공 여부
     */
    public boolean updateLectureField(Long lectureId, String field, String value) {
        return lectureManagementLectureDAO.updateLectureField(lectureId, field, value) > 0;
    }

    /**
     * 강의 상태(PUBLISHED, DRAFT) 변경
     * @param lectureId 강의 ID
     * @param status 새로운 상태
     * @return 성공 여부
     */
    public boolean updateStatus(Long lectureId, String status) {
        return lectureManagementLectureDAO.updateLectureStatus(lectureId, status) > 0;
    }

    /**
     * 강의 삭제 처리
     * @param lectureId 강의 ID
     * @return 성공 여부
     */
    public boolean deleteLecture(Long lectureId) {
        return lectureManagementLectureDAO.deleteLectureById(lectureId) > 0;
    }

    /**
     * 카테고리 목록 반환 (UI 필터용)
     * @return 카테고리 문자열 목록
     */
    public List<String> getAllCategories() {
        return lectureManagementLectureDAO.selectAllCategories();
    }
}

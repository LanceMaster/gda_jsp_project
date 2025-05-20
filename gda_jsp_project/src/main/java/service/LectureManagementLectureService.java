package service;

import model.dao.LectureManagementLectureDAO;
import model.dto.LectureManagementLectureDTO;

import java.util.List;

public class LectureManagementLectureService {

    private final LectureManagementLectureDAO dao = new LectureManagementLectureDAO();

    public List<LectureManagementLectureDTO> getLecturesByInstructorFiltered(
            Long instructorId, String search, String category, String status, String sort) {
        return dao.getLecturesByInstructorFiltered(instructorId, search, category, status, sort);
    }

    public List<String> getAllCategories() {
        return dao.getAllCategories();
    }
    
    public boolean updateLectureField(Long lectureId, String field, String value) {
        return dao.updateLectureField(lectureId, field, value);
    }
    
    public boolean updateStatus(Long lectureId, String status) {
        return dao.updateStatus(lectureId, status);
    }
    
    public boolean deleteLecture(Long lectureId) {
        return dao.deleteLecture(lectureId);
    }

}

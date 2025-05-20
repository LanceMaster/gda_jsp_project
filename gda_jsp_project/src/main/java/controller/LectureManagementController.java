package controller;

import model.dto.LectureManagementLectureDTO;
import service.LectureManagementLectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/lecture/management")
public class LectureManagementController extends HttpServlet {

    private final LectureManagementLectureService service = new LectureManagementLectureService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 세션에서 사용자 ID 가져오기
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long instructorId = (Long) session.getAttribute("userId");

        // 요청 파라미터 안전하게 추출
        String search = safeParam(req.getParameter("search"));
        String category = safeParam(req.getParameter("category"));
        String status = safeParam(req.getParameter("status"));
        String sort = safeParam(req.getParameter("sort"));

        // 강의 목록 조회 (필터 적용)
        List<LectureManagementLectureDTO> myLectures = service.getLecturesByInstructorFiltered(
                instructorId, search, category, status, sort
        );

        // JSP에 데이터 전달
        req.setAttribute("myLectures", myLectures);
        req.setAttribute("categories", service.getAllCategories()); // 카테고리 선택박스용

        // JSP 포워딩
        req.getRequestDispatcher("/view/lecture/lectureManagementPage.jsp").forward(req, resp);
    }

    // Null-safe 파라미터 처리 유틸
    private String safeParam(String param) {
        return (param != null && !param.trim().isEmpty()) ? param.trim() : null;
    }
}

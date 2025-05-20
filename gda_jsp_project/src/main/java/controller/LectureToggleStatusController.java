package controller;

import service.LectureManagementLectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/lecture/toggleStatus")
public class LectureToggleStatusController extends HttpServlet {

    private final LectureManagementLectureService service = new LectureManagementLectureService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long lectureId = Long.valueOf(req.getParameter("lectureId"));
        String status = req.getParameter("status");

        boolean result = service.updateStatus(lectureId, status);
        if (result) {
            resp.setStatus(200);
        } else {
            resp.sendError(400, "상태 변경 실패");
        }
    }
}

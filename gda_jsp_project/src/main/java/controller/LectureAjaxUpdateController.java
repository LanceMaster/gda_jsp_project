package controller;

import service.LectureManagementLectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/lecture/updateField")
public class LectureAjaxUpdateController extends HttpServlet {

    private final LectureManagementLectureService service = new LectureManagementLectureService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long lectureId = Long.valueOf(req.getParameter("lectureId"));
        String field = req.getParameter("field");
        String value = req.getParameter("value");

        boolean result = service.updateLectureField(lectureId, field, value);
        if (result) {
            resp.setStatus(200);
        } else {
            resp.sendError(400, "수정 실패");
        }
    }
}

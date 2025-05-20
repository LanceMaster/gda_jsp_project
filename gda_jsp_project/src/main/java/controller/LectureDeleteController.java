package controller;

import service.LectureManagementLectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/lecture/delete")
public class LectureDeleteController extends HttpServlet {

    private final LectureManagementLectureService service = new LectureManagementLectureService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long lectureId = Long.valueOf(req.getParameter("lectureId"));

        boolean result = service.deleteLecture(lectureId);
        if (result) {
            resp.setStatus(200);
        } else {
            resp.sendError(400, "삭제 실패");
        }
    }
}

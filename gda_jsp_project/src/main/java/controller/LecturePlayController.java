// ✅ 2. LecturePlayController.java
package controller;

import model.dao.LecturePlayDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/lecture/play")
public class LecturePlayController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int lectureId = Integer.parseInt(req.getParameter("lectureId"));
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LecturePlayDAO dao = new LecturePlayDAO(session);
            LectureDTO lecture = dao.selectLectureById(lectureId);
            List<ContentDTO> contents = dao.selectContentsByLectureId(lectureId);

            req.setAttribute("lecture", lecture);
            req.setAttribute("contents", contents);
            req.getRequestDispatcher("/view/lecture/lecturePlay.jsp").forward(req, resp);
        }
    }
}
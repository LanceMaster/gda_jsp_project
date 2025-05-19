// ✅ LecturePlayController.java
package controller;

import model.dao.LectureDAO;
import model.dao.TagDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.TagDTO;
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
            LectureDAO lectureDAO = new LectureDAO(session);
            TagDAO tagDAO = new TagDAO(session);

            // 1️⃣ 강의 기본 정보 + 썸네일
            LectureDTO lecture = lectureDAO.getLectureById(lectureId);

            // 2️⃣ 첫 번째 콘텐츠 (재생할 영상)
            ContentDTO content = lectureDAO.getFirstContentByLectureId(lectureId);

            // 3️⃣ 강의 관련 태그 리스트
            List<TagDTO> tags = tagDAO.selectTagsByTarget("LECTURE", lectureId);

            // 📦 JSP에 전달
            req.setAttribute("lecture", lecture);
            req.setAttribute("content", content);
            req.setAttribute("tags", tags);

            req.getRequestDispatcher("/view/lecture/lecturePlay.jsp").forward(req, resp);
        }
    }
}




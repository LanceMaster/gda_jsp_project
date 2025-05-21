// ✅ 2. LecturePlayController.java
package controller;

import model.dao.LecturePlayDAO;
import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.UserDTO;

import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,         // 1MB 메모리 임계
	    maxFileSize = 1024 * 1024 * 1000,         // 1000MB
	    maxRequestSize = 1024 * 1024 * 1000       // 1000MB
)
@WebServlet("/lecture/play")
public class LecturePlayController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int lectureId = Integer.parseInt(req.getParameter("lectureId"));
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession()) {
            LecturePlayDAO dao = new LecturePlayDAO(sqlSession);
            LectureDTO lecture = dao.selectLectureById(lectureId);

            List<ContentDTO> contents;
            if (user != null) {
                contents = dao.selectContentsWithProgress(lectureId, user.getUserId());
            } else {
                contents = dao.selectContentsByLectureId(lectureId); // 기존 로직
            }

            req.setAttribute("lecture", lecture);
            req.setAttribute("contents", contents);
            req.getRequestDispatcher("/view/lecture/lecturePlay.jsp").forward(req, resp);
        }
    }
    
    
    
}
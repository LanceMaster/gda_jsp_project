package controller;

import com.google.gson.Gson;
import model.dao.EnrollmentDAO;
import model.dao.ProgressLogDAO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lecture/progress/update")
public class ProgressAjaxController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();

        // ✅ 세션 로그인 확인
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            resp.getWriter().write(new Gson().toJson(result));
            return;
        }

        try {
            // ✅ 파라미터 파싱
            int userId = user.getUserId();
            int contentId = Integer.parseInt(req.getParameter("contentId"));
            int lectureId = Integer.parseInt(req.getParameter("lectureId"));
            int progress = Integer.parseInt(req.getParameter("progress"));

            try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
                ProgressLogDAO progressLogDAO = new ProgressLogDAO(sqlSession);
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO(sqlSession);

                // ✅ 수강 여부 확인
                if (!enrollmentDAO.isUserEnrolled(userId, lectureId)) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    result.put("success", false);
                    result.put("message", "수강 중인 강의가 아닙니다.");
                    resp.getWriter().write(new Gson().toJson(result));
                    return;
                }

                // ✅ 1. 진도 저장
                progressLogDAO.saveOrUpdateProgress(userId, contentId, progress);

                // ✅ 2. 수료 처리 조건 체크
                if (progress == 100) {
                    boolean completed = progressLogDAO.checkLectureCompletion(lectureId, userId);
                    if (completed) {
                        enrollmentDAO.markLectureAsCompleted(userId, lectureId);
                    }
                }

                // ✅ 성공 응답
                result.put("success", true);
                resp.getWriter().write(new Gson().toJson(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "서버 오류: " + e.getMessage());
            resp.getWriter().write(new Gson().toJson(result));
        }
    }
}

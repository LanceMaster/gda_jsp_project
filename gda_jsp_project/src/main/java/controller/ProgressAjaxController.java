package controller;

import com.google.gson.Gson;
import model.dao.EnrollmentDAO;
import model.dao.ProgressLogDAO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lecture/progress/update")
public class ProgressAjaxController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // ✅ 요청 파라미터 파싱
        int contentId = Integer.parseInt(req.getParameter("contentId"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        int progress = Integer.parseInt(req.getParameter("progress"));
        int lectureId = Integer.parseInt(req.getParameter("lectureId"));

        Map<String, Object> result = new HashMap<>();

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            // ✅ 수강 여부 확인
            boolean enrolled = new EnrollmentDAO(session).isUserEnrolled(userId, lectureId);
            if (!enrolled) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                result.put("success", false);
                result.put("message", "수강 중인 강의가 아닙니다.");
                resp.getWriter().write(new Gson().toJson(result));
                return;
            }

            // ✅ 진도 업데이트
            ProgressLogDAO progressDao = new ProgressLogDAO(session);
            progressDao.upsertProgress(userId, contentId, progress);

            // ✅ 성공 응답
            result.put("success", true);
            resp.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "서버 오류 발생");
            resp.getWriter().write(new Gson().toJson(result));
        }
    }
}

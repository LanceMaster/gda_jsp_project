package controller;

import com.google.gson.Gson;
import model.dto.UserDTO;
import service.ProgressService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 📡 /lecture/progress/update
 * - 강의 콘텐츠 진도율 업데이트 처리 (JSON 기반)
 */
@WebServlet("/lecture/progress/update")
public class ProgressAjaxController extends HttpServlet {

    private final ProgressService progressService = new ProgressService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        // ✅ 로그인 여부 확인
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            resp.getWriter().write(gson.toJson(result));
            return;
        }

        try {
            // ✅ JSON 요청 바디 파싱
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);

            int contentId = ((Double) data.get("contentId")).intValue();
            int progress = ((Double) data.get("progress")).intValue();

            // ✅ 진도율 저장/갱신
            progressService.saveOrUpdateProgress(user.getUserId(), contentId, progress);

            result.put("success", true);
            result.put("message", "진도율 업데이트 성공");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "진도율 처리 중 오류 발생");
        }

        resp.getWriter().write(gson.toJson(result));
    }
}

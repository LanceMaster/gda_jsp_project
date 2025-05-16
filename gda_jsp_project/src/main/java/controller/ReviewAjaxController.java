package controller;

import com.google.gson.Gson;
import model.dto.ReviewDTO;
import service.ReviewService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/review")
public class ReviewAjaxController extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");

        try {
            int lectureId = Integer.parseInt(req.getParameter("lectureId"));
            List<ReviewDTO> reviews = reviewService.getReviewsByLectureId(lectureId);
            gson.toJson(reviews, res.getWriter());
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "리뷰 조회 중 오류 발생");
            gson.toJson(error, res.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        try {
            HttpSession session = req.getSession();
            Integer userId = (Integer) session.getAttribute("loginUserId");

            if (userId == null) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                gson.toJson(result, res.getWriter());
                return;
            }

            int lectureId = Integer.parseInt(req.getParameter("lectureId"));
            String content = req.getParameter("content");
            int rating = Integer.parseInt(req.getParameter("rating"));

            ReviewDTO review = new ReviewDTO();
            review.setLectureId(lectureId); // ✅ targetId → lectureId
            review.setContent(content);
            review.setRating(rating);
            review.setUserId(userId);

            reviewService.addReview(review); // ✅ Service 계층 호출

            result.put("success", true);
            gson.toJson(result, res.getWriter());

        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "리뷰 등록 중 오류 발생");
            gson.toJson(result, res.getWriter());
        }
    }
}

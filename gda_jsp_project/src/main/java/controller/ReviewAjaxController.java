package controller;

import com.google.gson.Gson;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/review")
public class ReviewAjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int lectureId = Integer.parseInt(req.getParameter("lectureId"));
        List<ReviewDTO> reviews = new ReviewDAO().getReviewsByLectureId(lectureId);

        res.setContentType("application/json; charset=UTF-8");
        new Gson().toJson(reviews, res.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("loginUserId");

        if (userId == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setTargetId(Integer.parseInt(req.getParameter("lectureId")));
        dto.setContent(req.getParameter("content"));
        dto.setRating(Integer.parseInt(req.getParameter("rating")));
        dto.setUserId(userId);

        new ReviewDAO().insertReview(dto);

        res.setContentType("application/json");
        res.getWriter().write("{\"success\": true}");
    }
}

package controller;

import com.google.gson.Gson;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dao.LectureDAO;
import model.dao.TagDAO;
import model.dto.*;
import service.*;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(urlPatterns = { "/lecture/*" }, initParams = {
        @WebInitParam(name = "view", value = "/view/")
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 200,
        maxRequestSize = 1024 * 1024 * 500
)
public class LectureController extends MskimRequestMapping {

    private final LectureService lectureService = new LectureService();
    private final TagService tagService = new TagService();
    private final InquiryService inquiryService = new InquiryService();
    private final ReviewService reviewService = new ReviewService();
    private final LectureManagementLectureService managementService = new LectureManagementLectureService();
    private final Gson gson = new Gson();

    @RequestMapping("lecturedetail")
    public String lectureDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String param = request.getParameter("lectureId");

        if (param == null || !param.matches("\\d+")) {
            request.setAttribute("error", "잘못된 요청입니다.");
            return "error/errorPage";
        }

        int lectureId = Integer.parseInt(param);
        LectureDTO lecture = lectureService.getLectureById(lectureId);

        if (lecture == null) {
            request.setAttribute("error", "존재하지 않는 강의입니다.");
            return "error/errorPage";
        }

        List<ReviewDTO> reviewList = reviewService.getReviewsByLectureId(lectureId);
        request.setAttribute("lecture", lecture);
        request.setAttribute("reviewList", reviewList);

        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        boolean hasReviewed = false;
        boolean hasEnrolled = false;
        boolean canReview = false;
        boolean hasPurchased = false;

        if (user != null) {
            hasEnrolled = reviewService.hasEnrolled(user.getUserId(), lectureId);
            hasReviewed = reviewService.hasReviewed(user.getUserId(), lectureId);
            canReview = hasEnrolled && !hasReviewed;
            hasPurchased = lectureService.hasPurchasedLecture(user.getUserId(), lectureId);
        }

        request.setAttribute("hasPurchased", hasPurchased);
        request.setAttribute("hasEnrolled", hasEnrolled);
        request.setAttribute("hasReviewed", hasReviewed);
        request.setAttribute("canReview", canReview);

        return "lecture/lectureDetail";
    }

    @RequestMapping("management")
    public String management(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || !"INSTRUCTOR".equalsIgnoreCase(user.getRole())) {
            return "redirect:" + request.getContextPath() + "/user/loginform";
        }

        int instructorId = user.getUserId();
        String search = param(request, "search");
        String category = param(request, "category");
        String status = param(request, "status");
        String sort = param(request, "sort");

        List<LectureManagementLectureDTO> myLectures =
                managementService.getLecturesByInstructorFiltered(instructorId, search, category, status, sort);

        request.setAttribute("myLectures", myLectures);
        request.setAttribute("categories", managementService.getAllCategories());
        return "lecture/lectureManagementPage";
    }

    @RequestMapping("updateField")
    public String updateField(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long lectureId = longParam(request, "lectureId");
        String field = param(request, "field");
        String value = param(request, "value");

        boolean success = managementService.updateLectureField(lectureId, field, value);
        writeJson(response, Map.of("success", success));
        return null;
    }

    @RequestMapping("toggleStatus")
    public String toggleStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long lectureId = longParam(request, "lectureId");
        String status = param(request, "status");

        boolean success = managementService.updateStatus(lectureId, status);
        writeJson(response, Map.of("success", success));
        return null;
    }

    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long lectureId = longParam(request, "lectureId");

        boolean success = managementService.deleteLecture(lectureId);
        writeJson(response, Map.of("success", success));
        return null;
    }

    private String param(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    private Long longParam(HttpServletRequest req, String name) {
        try {
            return Long.parseLong(req.getParameter(name));
        } catch (Exception e) {
            return null;
        }
    }

    private void writeJson(HttpServletResponse resp, Map<String, Object> result) throws Exception {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(result));
        out.flush();
    }
} 

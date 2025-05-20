package controller;

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

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = { "/lecture/*" }, initParams = {
        @WebInitParam(name = "view", value = "/view/")
})
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,         // 1MB 메모리 임계
	    maxFileSize = 1024 * 1024 * 200,         // 200MB
	    maxRequestSize = 1024 * 1024 * 500       // 500MB
)
public class LectureController extends MskimRequestMapping {

    private final LectureService lectureService = new LectureService();
    private final TagService tagService = new TagService();
    private final InquiryService inquiryService = new InquiryService();
    private final ReviewService reviewService = new ReviewService();

    /**
     * ✅ 강의 목록 조회
     * - 키워드 검색 / 카테고리 필터 / 정렬 조건 모두 지원
     * - 내부적으로 조건에 따라 적절한 DAO 메서드로 분기 처리
     */


    @RequestMapping("lecturedetail")
    public String lectureDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String param = request.getParameter("lectureId");

        // 유효성 검사
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

        // 리뷰 목록 가져오기
        List<ReviewDTO> reviewList = reviewService.getReviewsByLectureId(lectureId);
        request.setAttribute("lecture", lecture);
        request.setAttribute("reviewList", reviewList);

        // 사용자 세션 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        boolean hasReviewed = false;
        boolean hasEnrolled = false;
        boolean canReview = false;
        //김준희 추가
        boolean hasPurchased = false;

        if (user != null) {
            hasEnrolled = reviewService.hasEnrolled(user.getUserId(), lectureId);
            hasReviewed = reviewService.hasReviewed(user.getUserId(), lectureId);
            canReview = hasEnrolled && !hasReviewed; // ✅ 진도율 조건 제거
            hasPurchased = lectureService.hasPurchasedLecture(user.getUserId(), lectureId);
        }

        request.setAttribute("hasPurchased", hasPurchased);
        request.setAttribute("hasEnrolled", hasEnrolled);
        request.setAttribute("hasReviewed", hasReviewed);
        request.setAttribute("canReview", canReview);

        return "lecture/lectureDetail";
    }


//    @RequestMapping("play")
//    public String lecturePlay(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        int lectureId = Integer.parseInt(request.getParameter("lectureId"));
//        LectureDTO lecture = lectureService.getLectureById(lectureId);
//        ContentDTO content = lectureService.getFirstContentByLectureId(lectureId);
//        List<TagDTO> tagList = tagService.getTagsByLectureId(lectureId);
//        request.setAttribute("lecture", lecture);
//        request.setAttribute("content", content);
//        request.setAttribute("tagList", tagList);
//        return "lecture/lecturePlay";
//    }


}

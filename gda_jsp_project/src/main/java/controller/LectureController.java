package controller;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dto.*;
import service.*;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = { "/lecture/*" }, initParams = {
        @WebInitParam(name = "view", value = "/view/")
})
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 100,
        maxRequestSize = 1024 * 1024 * 200
)
public class LectureController extends MskimRequestMapping {

    private final LectureService lectureService = new LectureService();
    private final TagService tagService = new TagService();
    private final InquiryService inquiryService = new InquiryService();
    private final ReviewService reviewService = new ReviewService();

    @RequestMapping("lecturelist")
    public String lectureList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String category = request.getParameter("category"); // 카테고리 필터
        String sort = request.getParameter("sort");         // 정렬 기준

        List<LectureDTO> lectures;

        // 🔍 category가 지정된 경우 → 정렬까지 함께 반영
        if (category != null && !category.isBlank()) {
            lectures = lectureService.getLecturesByCategorySorted(category, sort);
        } else {
            lectures = lectureService.getAllLecturesSorted(sort);
        }

        // 📦 데이터 전달
        request.setAttribute("lectures", lectures);
        request.setAttribute("param", request.getParameterMap()); // 🔁 파라미터 유지용
        return "lecture/lectureList";
    }



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

        return "lecture/lectureDetail";
    }

    @RequestMapping("play")
    public String lecturePlay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));
        LectureDTO lecture = lectureService.getLectureById(lectureId);
        ContentDTO content = lectureService.getFirstContentByLectureId(lectureId);
        List<TagDTO> tagList = tagService.getTagsByLectureId(lectureId);
        request.setAttribute("lecture", lecture);
        request.setAttribute("content", content);
        request.setAttribute("tagList", tagList);
        return "lecture/lecturePlay";
    }

    @RequestMapping("lectureUpload")
    public String lectureUploadForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "lecture/lectureUpload";
    }

    @RequestMapping("uploadSubmit")
    public String uploadSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String title = request.getParameter("lectureTitle");
        String description = request.getParameter("lectureDescription");
        int price = Integer.parseInt(request.getParameter("price"));
        int orderNo = Integer.parseInt(request.getParameter("orderNo"));
        int duration = Integer.parseInt(request.getParameter("duration"));

        Part contentPart = request.getPart("contentFile");
        Part thumbnailPart = request.getPart("thumbnailFile");

        String contentFileName = Paths.get(contentPart.getSubmittedFileName()).getFileName().toString();
        String thumbnailFileName = Paths.get(thumbnailPart.getSubmittedFileName()).getFileName().toString();

        String contentSavedName = UUID.randomUUID() + "_" + contentFileName;
        String thumbnailSavedName = UUID.randomUUID() + "_" + thumbnailFileName;

        String contentPath = request.getServletContext().getRealPath("/upload/video/") + contentSavedName;
        String thumbnailPath = request.getServletContext().getRealPath("/upload/thumb/") + thumbnailSavedName;

        contentPart.write(contentPath);
        thumbnailPart.write(thumbnailPath);

        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setTitle(title);
        lectureDTO.setDescription(description);
        lectureDTO.setThumbnail("/upload/thumb/" + thumbnailSavedName);
        lectureDTO.setPrice(price);

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTitle(title + " - part 1");
        contentDTO.setUrl("/upload/video/" + contentSavedName);
        contentDTO.setType("VIDEO");
        contentDTO.setOrderNo(orderNo);
        contentDTO.setDuration(duration);

        lectureService.registerLectureWithContent(lectureDTO, contentDTO);
        return "redirect:/view/user/mainpage.jsp";
    }
}

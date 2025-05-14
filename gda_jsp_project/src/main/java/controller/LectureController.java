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

        String category = request.getParameter("category");
        String keyword = request.getParameter("keyword");

        List<LectureDTO> lectures;

        // 🔍 조건 분기: category → keyword → 전체
        if (category != null && !category.isBlank()) {
            lectures = lectureService.getLecturesByCategory(category);
        } else if (keyword != null && !keyword.isBlank()) {
            lectures = lectureService.searchLecturesByKeyword(keyword);
        } else {
            lectures = lectureService.getAllLectures();
        }

        request.setAttribute("lectures", lectures);
        return "lecture/lectureList";
    }


    @RequestMapping("lecturedetail")
    public String lectureDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));
        LectureDTO lecture = lectureService.getLectureById(lectureId);
        List<ReviewDTO> reviewList = reviewService.getReviewsByLectureId(lectureId);
        request.setAttribute("lecture", lecture);
        request.setAttribute("reviewList", reviewList);
        return "lecture/lectureDetail";
    }

    @RequestMapping("inquiries")
    public String inquiryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int size = 10;
        int offset = (page - 1) * size;
        List<InquiryDTO> inquiryList = inquiryService.getAllInquiries(size, offset);
        int totalCount = inquiryService.getInquiryCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        request.setAttribute("inquiryList", inquiryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        return "lecture/inquiryList";
    }

    @RequestMapping("inquiry/detail")
    public String inquiryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        InquiryDTO inquiry = inquiryService.getInquiryById(inquiryId);
        request.setAttribute("inquiry", inquiry);
        return "lecture/inquiryDetail";
    }

    @RequestMapping("inquiry/write")
    public String inquiryWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/loginform.jsp";

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            request.setAttribute("error", "제목과 내용을 모두 입력해주세요.");
            return "lecture/inquiryWrite";
        }

        InquiryDTO dto = new InquiryDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setLectureId(lectureId);
        dto.setUserId(loginUser.getUserId());
        dto.setCreatedAt(LocalDateTime.now());

        inquiryService.registerInquiry(dto);
        return "redirect:/lecture/inquiries?lectureId=" + lectureId;
    }

    @RequestMapping("inquiry/update")
    public String inquiryUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/user/loginform.jsp";

        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            request.setAttribute("error", "제목과 내용을 모두 입력해주세요.");
            request.setAttribute("inquiry", inquiryService.getInquiryById(inquiryId));
            request.setAttribute("lectureId", lectureId);
            return "lecture/inquiryEdit";
        }

        InquiryDTO dto = new InquiryDTO();
        dto.setInquiryId(inquiryId);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setLectureId(lectureId);
        dto.setUserId(loginUser.getUserId());

        inquiryService.updateInquiry(dto);
        return "redirect:/lecture/inquiries?lectureId=" + lectureId;
    }

    @RequestMapping("inquiry/delete")
    public String inquiryDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));
        inquiryService.deleteInquiry(inquiryId);
        return "redirect:/lecture/inquiries?lectureId=" + lectureId;
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

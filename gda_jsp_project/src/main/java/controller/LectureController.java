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

//    @RequestMapping("lectureUpload")
//    public String lectureUploadForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        List<TagDTO> tagList = tagService.getAllTags();         // ✅ 전체 태그 목록 조회
//        request.setAttribute("tagList", tagList);               // ✅ JSP에 전달
//        return "lecture/lectureUpload";
//    }
////
//    @RequestMapping("uploadSubmit")
//    public String uploadSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
//        if (loginUser == null || !"INSTRUCTOR".equals(loginUser.getRole())) {
//            return "redirect:/user/loginform";
//        }
//        int userId = loginUser.getUserId();
//
//        String title = request.getParameter("lectureTitle");
//        String description = request.getParameter("lectureDescription");
//        String[] tagIds = request.getParameterValues("tags");
//        String curriculum = request.getParameter("curriculum");
//
//        int price = Integer.parseInt(request.getParameter("price"));
//        int orderNo = Integer.parseInt(request.getParameter("orderNo") != null ? request.getParameter("orderNo") : "1");
//        int duration = Integer.parseInt(request.getParameter("duration") != null ? request.getParameter("duration") : "0");
//
//        Part contentPart = request.getPart("contentFile");
//        Part thumbnailPart = request.getPart("thumbnailFile");
//
//        String contentFileName = Paths.get(contentPart.getSubmittedFileName()).getFileName().toString();
//        String thumbnailFileName = Paths.get(thumbnailPart.getSubmittedFileName()).getFileName().toString();
//
//        String contentSavedName = UUID.randomUUID() + "_" + contentFileName;
//        String thumbnailSavedName = UUID.randomUUID() + "_" + thumbnailFileName;
//
//        String contentDir = request.getServletContext().getRealPath("/upload/video/");
//        String thumbDir = request.getServletContext().getRealPath("/upload/thumb/");
//
//        // ✅ 디렉토리 존재 확인 및 생성
//        new java.io.File(contentDir).mkdirs();
//        new java.io.File(thumbDir).mkdirs();
//
//        String contentPath = contentDir + contentSavedName;
//        String thumbnailPath = thumbDir + thumbnailSavedName;
//
//        try {
//            contentPart.write(contentPath);
//            thumbnailPart.write(thumbnailPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "파일 업로드 중 오류 발생: " + e.getMessage());
//            return "error/errorPage";
//        }
//
//        // DTO 구성
//        LectureDTO lectureDTO = new LectureDTO();
//        lectureDTO.setTitle(title);
//        lectureDTO.setDescription(description);
//        lectureDTO.setCurriculum(curriculum);
//        lectureDTO.setThumbnail("/upload/thumb/" + thumbnailSavedName);
//        lectureDTO.setPrice(price);
//        lectureDTO.setInstructorId(userId);
//
//        ContentDTO contentDTO = new ContentDTO();
//        contentDTO.setTitle(title + " - part 1");
//        contentDTO.setUrl("/upload/video/" + contentSavedName);
//        contentDTO.setType("VIDEO");
//        contentDTO.setOrderNo(orderNo);
//        contentDTO.setDuration(duration);
//
//        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
//            LectureDAO lectureDAO = new LectureDAO(session);
//            TagDAO tagDAO = new TagDAO(session);
//
//            lectureDAO.insertLecture(lectureDTO);
//            contentDTO.setLectureId(lectureDTO.getLectureId());
//            lectureDAO.insertContent(contentDTO);
//
//            if (tagIds != null) {
//                for (String tagIdStr : tagIds) {
//                    int tagId = Integer.parseInt(tagIdStr);
//                    tagDAO.insertMapping(lectureDTO.getLectureId(), "LECTURE", tagId);
//                }
//            }
//
//            session.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "DB 저장 중 오류 발생: " + e.getMessage());
//            return "error/errorPage";
//        }
//
//        return "lecture/lectureList";
//    }

}

package controller;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import service.LectureUploadService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(
    urlPatterns = {"/lecture/uploadSubmit"},
    initParams = {
        @WebInitParam(name = "view", value = "/view/")
    }
)
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1MB
    maxFileSize = 1024 * 1024 * 1000, // 1GB
    maxRequestSize = 1024 * 1024 * 1000
)
public class LectureUploadController extends HttpServlet {

    private final LectureUploadService lectureService = new LectureUploadService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 로그인 및 권한 확인
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser == null || !"INSTRUCTOR".equals(loginUser.getRole())) {
            response.sendRedirect("/user/loginform");
            return;
        }

        int userId = loginUser.getUserId();

        // ✅ 폼 파라미터 수집
        String title = request.getParameter("lectureTitle");
        String description = request.getParameter("lectureDescription");
        String curriculum = request.getParameter("curriculum");
        String category = request.getParameter("category");
        int price = Integer.parseInt(request.getParameter("price"));
        int duration = Integer.parseInt(request.getParameter("duration"));
        int orderNo = Integer.parseInt(request.getParameter("orderNo"));
        String[] tagIds = request.getParameterValues("tags");  // nullable

        if (orderNo < 1 || duration < 1) {
            request.setAttribute("error", "재생 시간과 순서는 1 이상이어야 합니다.");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
            return;
        }
        
        // ✅ 파일 업로드 처리
        Part contentPart = request.getPart("contentFile");
        Part thumbPart = request.getPart("thumbnailFile");

        String contentFileName = Paths.get(contentPart.getSubmittedFileName()).getFileName().toString();
        String thumbnailFileName = Paths.get(thumbPart.getSubmittedFileName()).getFileName().toString();

        String contentUUID = UUID.randomUUID() + "_" + contentFileName;
        String thumbUUID = UUID.randomUUID() + "_" + thumbnailFileName;

        String videoSavePath = request.getServletContext().getRealPath("/upload/video/");
        String thumbSavePath = request.getServletContext().getRealPath("/upload/thumb/");

        new File(videoSavePath).mkdirs();
        new File(thumbSavePath).mkdirs();

        try {
            contentPart.write(videoSavePath + File.separator + contentUUID);
            thumbPart.write(thumbSavePath + File.separator + thumbUUID);
        } catch (Exception e) {
            request.setAttribute("error", "파일 저장 중 오류 발생: " + e.getMessage());
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
            return;
        }

        // ✅ DTO 구성
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setTitle(title);
        lectureDTO.setDescription(description);
        lectureDTO.setCurriculum(curriculum);
        lectureDTO.setThumbnail("/upload/thumb/" + thumbUUID);
        lectureDTO.setPrice(price);
        lectureDTO.setInstructorId(userId);
        lectureDTO.setCategory(category);

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTitle(title + " - part 1");
        contentDTO.setType("VIDEO");
        contentDTO.setUrl("/upload/video/" + contentUUID);
        contentDTO.setOrderNo(orderNo);
        contentDTO.setDuration(duration);

        // ✅ 서비스 호출 (트랜잭션 포함)
        boolean success = lectureService.registerLectureWithContentAndTags(lectureDTO, contentDTO, tagIds);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/lecture/lecturelist");
        } else {
            request.setAttribute("error", "강의 등록 중 문제가 발생했습니다.");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
        }
    }
}

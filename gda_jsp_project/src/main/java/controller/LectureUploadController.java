package controller;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import service.LectureUploadService;
import utils.FileUploadUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/lecture/uploadSubmit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 1000,
    maxRequestSize = 1024 * 1024 * 1000
)
public class LectureUploadController extends HttpServlet {

    private final LectureUploadService lectureService = new LectureUploadService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ✅ 로그인 확인
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null || !"INSTRUCTOR".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/loginform");
            return;
        }

        // ✅ 파라미터 수집
        String title = request.getParameter("lectureTitle");
        String description = request.getParameter("lectureDescription");
        String curriculum = request.getParameter("curriculum");
        String category = request.getParameter("category");
        int price = Integer.parseInt(request.getParameter("price"));
        int duration = Integer.parseInt(request.getParameter("duration"));
        int orderNo = Integer.parseInt(request.getParameter("orderNo"));
        String[] tagIds = request.getParameterValues("tags");

        if (duration < 1 || orderNo < 1) {
            request.setAttribute("error", "재생 시간과 순서는 1 이상이어야 합니다.");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
            return;
        }

        // ✅ 파일 업로드 처리
        Part videoPart = request.getPart("contentFile");
        Part thumbPart = request.getPart("thumbnailFile");

        ServletContext context = request.getServletContext();

        // 파일 저장 후 상대 URL 반환
        String videoUrl = FileUploadUtil.saveFile("video", videoPart.getSubmittedFileName(), videoPart.getInputStream().readAllBytes(), context);
        String thumbUrl = FileUploadUtil.saveFile("thumb", thumbPart.getSubmittedFileName(), thumbPart.getInputStream().readAllBytes(), context);

        // ✅ DTO 생성
        LectureDTO lecture = new LectureDTO();
        lecture.setTitle(title);
        lecture.setDescription(description);
        lecture.setCurriculum(curriculum);
        lecture.setCategory(category);
        lecture.setThumbnail(thumbUrl);
        lecture.setInstructorId(loginUser.getUserId());
        lecture.setPrice(price);

        ContentDTO content = new ContentDTO();
        content.setTitle(title + " - Part 1");
        content.setLectureId(0); // FK는 서비스 내에서 처리
        content.setUrl(videoUrl);
        content.setType("VIDEO");
        content.setDuration(duration);
        content.setOrderNo(orderNo);

        // ✅ 서비스 호출 (트랜잭션 포함)
        boolean result = lectureService.registerLectureWithContentAndTags(lecture, content, tagIds);

        if (result) {
            response.sendRedirect(request.getContextPath() + "/lecture/lecturelist");
        } else {
            request.setAttribute("error", "강의 등록 중 문제가 발생했습니다.");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
        }
    }
}

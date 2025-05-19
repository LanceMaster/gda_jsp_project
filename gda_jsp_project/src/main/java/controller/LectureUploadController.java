package controller;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import service.LectureUploadService;
import utils.FileUploadUtil;
import utils.FFmpegUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@WebServlet("/lecture/uploadSubmit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 1000,
    maxRequestSize = 1024 * 1024 * 1000
)
public class LectureUploadController extends HttpServlet {

    private static final String LOCAL_UPLOAD_DIR = "C:/lecture_uploads"; // ★ 로컬 저장 위치

    private final LectureUploadService lectureService = new LectureUploadService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null || !"INSTRUCTOR".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/loginform");
            return;
        }

        // 📌 파라미터 수집
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

        // ✅ 파일 파트 수집
        Part videoPart = request.getPart("contentFile");
        Part thumbPart = request.getPart("thumbnailFile");

        // ✅ 썸네일 저장
        String thumbFileName = UUID.randomUUID() + "_" + thumbPart.getSubmittedFileName();
        InputStream thumbInputStream = thumbPart.getInputStream();
        byte[] thumbBytes = thumbInputStream.readAllBytes();
        thumbInputStream.close(); // ✅ 명시적으로 닫기

        String thumbUrl = FileUploadUtil.saveFileToLocal(
            "thumb", thumbFileName, thumbBytes, LOCAL_UPLOAD_DIR
        );

        // ✅ 영상 저장 + HLS 변환
        String uuid = UUID.randomUUID().toString();
        String videoFileName = uuid + "_" + videoPart.getSubmittedFileName();

        // 임시 저장
        File tempDir = new File(LOCAL_UPLOAD_DIR + "/temp");
        if (!tempDir.exists()) tempDir.mkdirs();
        File tempVideoFile = new File(tempDir, videoFileName);
        videoPart.write(tempVideoFile.getAbsolutePath());

        // HLS 변환
        String hlsUrl;
        try {
            File hlsOutputDir = new File(LOCAL_UPLOAD_DIR + "/hls");
            if (!hlsOutputDir.exists()) hlsOutputDir.mkdirs();
            hlsUrl = FFmpegUtil.convertToHLS(tempVideoFile, uuid, hlsOutputDir.getAbsolutePath());
        } catch (Exception e) {
            request.setAttribute("error", "영상 변환 중 오류 발생: " + e.getMessage());
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
            return;
        }

        // DTO 구성
        LectureDTO lecture = new LectureDTO();
        lecture.setTitle(title);
        lecture.setDescription(description);
        lecture.setCurriculum(curriculum);
        lecture.setCategory(category);
        lecture.setThumbnail(thumbUrl);  // /upload/thumb/....jpg
        lecture.setInstructorId(loginUser.getUserId());
        lecture.setPrice(price);

        ContentDTO content = new ContentDTO();
        content.setTitle(title + " - Part 1");
        content.setUrl(hlsUrl); // /upload/hls/UUID.m3u8
        content.setType("VIDEO");
        content.setDuration(duration);
        content.setOrderNo(orderNo);

        boolean result = lectureService.registerLectureWithContentAndTags(lecture, content, tagIds);

        if (result) {
            response.sendRedirect(request.getContextPath() + "/lecture/lecturelist");
        } else {
            request.setAttribute("error", "강의 등록 중 문제가 발생했습니다.");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
        }
    }
}

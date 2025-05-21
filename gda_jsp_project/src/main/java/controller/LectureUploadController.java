package controller;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import service.LectureUploadService;
import utils.FFmpegUtil;
import utils.FileUploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

@WebServlet("/lecture/uploadSubmit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 1024,
    maxRequestSize = 1024 * 1024 * 1024
)
public class LectureUploadController extends HttpServlet {

    private static final String LOCAL_UPLOAD_DIR = "C:/lecture_uploads";
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

        // ✅ 기본 정보 수집
        String title = request.getParameter("lectureTitle");
        String description = request.getParameter("lectureDescription");
        String curriculum = request.getParameter("curriculum");
        String category = request.getParameter("category");
        int price = Integer.parseInt(request.getParameter("price"));
        String[] orderNos = request.getParameterValues("orderNos");
        String[] contentTitles = request.getParameterValues("contentTitles");
        String[] tagIds = request.getParameterValues("tags");

        // ✅ 썸네일 저장
        Part thumbPart = request.getPart("thumbnailFile");
        String originalThumbName = thumbPart.getSubmittedFileName();
        String sanitizedThumbName = originalThumbName.replaceAll("\\s+", "_"); // 공백 제거
        String thumbFileName = UUID.randomUUID() + "_" + sanitizedThumbName;
        byte[] thumbBytes = thumbPart.getInputStream().readAllBytes();
        String thumbUrl = FileUploadUtil.saveFileToLocal("thumb", thumbFileName, thumbBytes, LOCAL_UPLOAD_DIR);

        // ✅ 콘텐츠 처리
        List<ContentDTO> contentList = new ArrayList<>();
        Collection<Part> parts = request.getParts();
        int index = 0;

        for (Part part : parts) {
            if (!"contentFiles".equals(part.getName()) || part.getSize() == 0) continue;

            String originalFileName = part.getSubmittedFileName();
            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + originalFileName;

            // temp 디렉토리 생성
            File tempDir = new File(LOCAL_UPLOAD_DIR + "/temp");
            if (!tempDir.exists()) tempDir.mkdirs();

            File tempFile = new File(tempDir, savedName);
            part.write(tempFile.getAbsolutePath());

            // hls 디렉토리 생성
            File hlsDir = new File(LOCAL_UPLOAD_DIR + "/hls");
            if (!hlsDir.exists()) hlsDir.mkdirs();

            // HLS 변환 및 URL 획득
            String hlsUrl;
            try {
                hlsUrl = FFmpegUtil.convertToHLS(tempFile, uuid, hlsDir.getAbsolutePath());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ServletException("HLS 변환 중 인터럽트 발생", e);
            }

            // ✅ 자동 영상 길이 추출
            int duration = FFmpegUtil.getVideoDurationInSeconds(tempFile);

            // ContentDTO 구성
            ContentDTO content = new ContentDTO();
            content.setTitle(contentTitles[index]);
            content.setUrl(hlsUrl);
            content.setDuration(duration);
            content.setOrderNo(Integer.parseInt(orderNos[index]));
            content.setType("VIDEO");

            contentList.add(content);
            index++;
        }

        // ✅ 강의 DTO 구성
        LectureDTO lecture = new LectureDTO();
        lecture.setTitle(title);
        lecture.setDescription(description);
        lecture.setCurriculum(curriculum);
        lecture.setCategory(category);
        lecture.setThumbnail(thumbUrl);
        lecture.setInstructorId(loginUser.getUserId());
        lecture.setPrice(price);

        // ✅ 서비스 처리
        boolean result = lectureService.registerLectureWithContentsAndTags(lecture, contentList, tagIds);

        if (result) {
            response.sendRedirect(request.getContextPath() + "/lecture/lecturelist");
        } else {
            request.setAttribute("error", "강의 등록 중 오류 발생");
            request.getRequestDispatcher("/view/error/errorPage.jsp").forward(request, response);
        }
    }
}

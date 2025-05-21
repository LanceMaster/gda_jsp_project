package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

/**
 * 브라우저에서 /upload/thumb/파일명 요청 시,
 * 실제 경로 (ex: C:/lecture_uploads/thumb/파일명) 에서 이미지를 찾아 바이너리로 응답
 */
@WebServlet("/upload/thumb/*")
public class ThumbnailServlet extends HttpServlet {

    // 실제 썸네일 파일이 저장된 외부 디렉토리 경로
    private static final String BASE_DIR = "C:/lecture_uploads/thumb";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ex) /UUID_파일명.png
        String pathInfo = req.getPathInfo(); 

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일명을 지정해주세요.");
            return;
        }

        // pathInfo에 포함된 경로 정제 (보안 상 ../ 접근 방지)
        pathInfo = pathInfo.replace("/", "").replace("\\", "");

        // 최종 파일 객체 생성
        File file = new File(BASE_DIR, pathInfo);

        // 파일 존재 여부 체크
        if (!file.exists() || file.isDirectory()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 파일을 찾을 수 없습니다.");
            return;
        }

        // MIME 타입 설정 (이미지, 영상 등 자동 판단)
        String mimeType = req.getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // fallback
        }

        // HTTP 응답 헤더 설정
        resp.setContentType(mimeType);
        resp.setContentLengthLong(file.length());

        // 파일 바이너리 전송
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}

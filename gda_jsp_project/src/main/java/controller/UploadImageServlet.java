package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/projects/uploadImage")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part filePart = request.getPart("file");
        if (filePart == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("파일이 없습니다.");
            return;
        }

        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }

        String safeFileName = UUID.randomUUID().toString() + extension;

        // ✅ 톰캣 webapps/static/images 경로 기준
        String uploadPath = getServletContext().getRealPath("/static/images");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filePath = uploadPath + File.separator + safeFileName;
        filePart.write(filePath);

        String imageUrl = request.getContextPath() + "/static/images/" + safeFileName;
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(imageUrl);
    }
}

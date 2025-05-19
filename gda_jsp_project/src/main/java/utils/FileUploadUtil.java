package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * ✅ FileUploadUtil
 * - 파일 저장 유틸리티 클래스
 * - 로컬 디스크에 파일 저장 후 JSP 출력용 URL 반환
 */
public class FileUploadUtil {

    /**
     * 📌 실제 파일 저장 + JSP 출력용 URL 반환
     * @param subDir "thumb" 또는 "video" 등 하위 폴더명
     * @param fileName 저장할 파일 이름 (UUID 포함 권장)
     * @param fileBytes 저장할 바이트 배열
     * @param baseDir 루트 저장 경로 (예: C:/lecture_uploads)
     * @return JSP 출력용 URL 경로 (예: /upload/thumb/...)
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public static String saveFileToLocal(String subDir, String fileName, byte[] fileBytes, String baseDir) throws IOException {
        File dir = new File(baseDir + "/" + subDir);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        }

        // 💡 JSP에서 접근 가능한 상대 경로 반환
        return "/upload/" + subDir + "/" + fileName;
    }
}

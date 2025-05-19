package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * ✅ FFmpegUtil
 * - mp4 → HLS(m3u8) 변환 유틸리티
 * - JSP 접근 경로 반환
 * - 오류 발생 시 로그 저장
 */
public class FFmpegUtil {

    private static final Logger logger = Logger.getLogger(FFmpegUtil.class.getName());

    // ❗ 환경에 맞게 수정: ffmpeg 실행 파일의 절대 경로
    private static final String FFMPEG_PATH = "C:/ffmpeg/bin/ffmpeg.exe";

    /**
     * ✅ mp4 파일을 HLS(.m3u8)로 변환
     *
     * @param inputFile 변환할 mp4 파일
     * @param uuid 출력 파일 이름 (확장자 없이)
     * @param outputDir 변환된 HLS 파일이 저장될 디렉토리 (절대경로)
     * @return JSP에서 접근 가능한 상대경로 (/upload/hls/uuid.m3u8)
     * @throws IOException 변환 실패 시 예외 발생
     */
    public static String convertToHLS(File inputFile, String uuid, String outputDir) throws IOException, InterruptedException
 {
        String m3u8FileName = uuid + ".m3u8";
        String fullOutputPath = Paths.get(outputDir, m3u8FileName).toString();

        // ffmpeg 명령어 구성
        ProcessBuilder pb = new ProcessBuilder(
                FFMPEG_PATH,
                "-i", inputFile.getAbsolutePath(),
                "-c:v", "libx264",
                "-c:a", "aac",
                "-start_number", "0",
                "-hls_time", "10",
                "-hls_list_size", "0",
                "-f", "hls",
                fullOutputPath
        );

        pb.redirectErrorStream(true); // 에러 출력도 함께 읽음

        try {
            Process process = pb.start();

            // 출력 로그 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("[ffmpeg] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg 변환 실패. 종료 코드: " + exitCode);
            }

            // 상대경로 리턴 (/upload/hls/uuid.m3u8)
            return "/upload/hls/" + m3u8FileName;

        } catch (Exception e) {
            logger.severe("FFmpeg 변환 중 오류: " + e.getMessage());
            throw new IOException("FFmpeg 변환 중 오류 발생", e);
        } finally {
            // 임시 mp4 파일 삭제
            try {
                Files.deleteIfExists(inputFile.toPath());
            } catch (IOException ex) {
                logger.warning("임시 파일 삭제 실패: " + inputFile.getName());
            }
        }
    }
}

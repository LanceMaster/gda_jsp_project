package utils;

import java.io.File;
import java.io.IOException;

/**
 * ✅ FFmpegUtil
 * - FFmpeg를 이용해 mp4 파일을 HLS(m3u8)로 변환하는 유틸리티
 */
public class FFmpegUtil {

    // Windows FFmpeg 실행 파일 경로 (환경에 따라 절대경로 설정 필요)
    private static final String FFMPEG_PATH = "C:/ffmpeg/bin/ffmpeg.exe";

    /**
     * 📌 mp4 파일을 HLS(m3u8)로 변환하고 JSP 접근 가능한 URL 반환
     * @param inputFile 원본 mp4 파일
     * @param uuid 고유 ID (m3u8 파일 이름으로 사용)
     * @param outputDir HLS 저장 폴더 (예: C:/lecture_uploads/hls)
     * @return JSP에서 접근 가능한 HLS URL (예: /upload/hls/UUID.m3u8)
     * @throws IOException 변환 실패 시 예외 발생
     */
    public static String convertToHLS(File inputFile, String uuid, String outputDir) throws IOException, InterruptedException {
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) outputFolder.mkdirs();

        // 출력 파일 경로 (C:/lecture_uploads/hls/UUID.m3u8)
        String outputPath = outputDir + "/" + uuid + ".m3u8";

        ProcessBuilder pb = new ProcessBuilder(
        	    FFMPEG_PATH,
        	    "-i", inputFile.getAbsolutePath(),
        	    "-c", "copy",                      // ✅ 올바른 옵션
        	    "-start_number", "0",
        	    "-hls_time", "10",
        	    "-hls_list_size", "0",
        	    "-f", "hls",
        	    outputPath
        	);


        pb.redirectErrorStream(true);  // 에러 출력 통합
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("FFmpeg 변환 실패. exit code: " + exitCode);
        }

        // ✅ JSP 접근용 URL 반환
        return "/upload/hls/" + uuid + ".m3u8";
    }
}

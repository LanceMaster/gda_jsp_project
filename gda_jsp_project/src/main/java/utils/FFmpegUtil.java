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
    private static final String FFMPEG_PATH = "C:/ffmpeg/bin/ffmpeg.exe";
    private static final String FFPROBE_PATH = "C:/ffmpeg/bin/ffprobe.exe"; // 🆕 추가

    /**
     * ✅ mp4 → HLS 변환
     */
    public static String convertToHLS(File inputFile, String uuid, String outputDir)
            throws IOException, InterruptedException {
        String m3u8FileName = uuid + ".m3u8";
        String fullOutputPath = Paths.get(outputDir, m3u8FileName).toString();

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

        pb.redirectErrorStream(true);
        Process process = pb.start();

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

        return "/upload/hls/" + m3u8FileName;
    }

    /**
     * ✅ ffprobe를 이용한 영상 길이(초) 추출
     */
    public static int getVideoDurationInSeconds(File videoFile) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                FFPROBE_PATH,
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine();
            if (line != null) {
                double duration = Double.parseDouble(line);
                return (int) Math.round(duration);
            } else {
                throw new IOException("영상 길이 분석 실패: 출력 없음");
            }
        } catch (NumberFormatException e) {
            throw new IOException("영상 길이 분석 실패: 숫자 형식 오류", e);
        }
    }
}


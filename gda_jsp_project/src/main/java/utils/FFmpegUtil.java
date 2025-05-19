package utils;

import java.io.File;
import java.io.IOException;

public class FFmpegUtil {

    public static String convertToHLS(File inputFile, String saveName, String outputDir) throws IOException, InterruptedException {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        String m3u8Path = outputDir + "/" + saveName + ".m3u8";

        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg", "-i", inputFile.getAbsolutePath(),
            "-codec:", "copy",
            "-start_number", "0",
            "-hls_time", "10",
            "-hls_list_size", "0",
            "-f", "hls",
            m3u8Path
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) return "/upload/hls/" + saveName + ".m3u8";
        else throw new RuntimeException("⚠️ FFmpeg 변환 실패");
    }
}

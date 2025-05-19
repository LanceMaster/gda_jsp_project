package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;

public class FileUploadUtil {

    public static String saveFile(String fileType, String originalFilename, byte[] fileBytes, ServletContext context) throws IOException {
    	String subFolder = fileType.toLowerCase().equals("video") ? "/upload/video" : "/upload/thumb";
        String realPath = context.getRealPath(subFolder);

        File dir = new File(realPath);
        if (!dir.exists()) dir.mkdirs();

        String ext = FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + "_" + originalFilename;

        File saveFile = new File(dir, saveName);
        Files.write(saveFile.toPath(), fileBytes);

        return subFolder + "/" + saveName;
    }
}

package fun.xianlai.core.utils.file;

import fun.xianlai.core.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * 复用org.apache.commons.io.FilenameUtils里的工具
 *
 * @author WyattLau
 */
public class FilenameUtils extends org.apache.commons.io.FilenameUtils {
    /**
     * 获取文件的拓展名
     */
    public static String getExtensionOfFile(MultipartFile file) {
        String extension = org.apache.commons.io.FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isBlank(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}

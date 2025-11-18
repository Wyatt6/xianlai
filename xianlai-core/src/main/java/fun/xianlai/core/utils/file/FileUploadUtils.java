package fun.xianlai.core.utils.file;

import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * @author WyattLau
 */
@Slf4j
public class FileUploadUtils {
    public static final long DEFAULT_FILE_MAX_SIZE = 100 * 1024 * 1024L;    // 默认的文件最大尺寸（单位KB），默认值是100MB
    public static final int DEFAULT_FILENAME_MAX_LEN = 100;                 // 默认的文件名（前缀+文件名+后缀，不含拓展名）最大长度

    /**
     * 上传文件
     *
     * @param file             文件(not null)
     * @param fileMaxSize      文件最大尺寸（单位B，为空时使用DEFAULT_FILE_MAX_SIZE）
     * @param baseDir          文件存储的基路径(not blank)
     * @param prefix           文件名前缀
     * @param name             文件名（null时使用自动生成的文件名）
     * @param suffix           文件名（不含拓展名）
     * @param allowedExtension 允许的文件类型（拓展名）
     * @return 返回上传成功的文件名（不含前缀路径但含拓展名）
     */
    public static String uploadFile(MultipartFile file, Long fileMaxSize,
                                    String baseDir, String prefix, String name, String suffix, String[] allowedExtension) {
        // 检查文件对象
        if (file == null) {
            throw new SysException("文件不能为空");
        }
        // 检查文件大小
        checkFileSize(file, fileMaxSize);
        // 检查存储路径
        baseDir = StringUtils.trimToNull(baseDir);
        if (baseDir == null) {
            throw new SysException("文件保存路径不能为空");
        }
        // 检查文件名
        String filename = name == null ? generateUuidFilename(prefix, suffix)
                : StringUtils.trimToNull(StringUtils.trimToEmpty(prefix) + name + StringUtils.trimToEmpty(suffix));
        if (filename == null) {
            throw new SysException("文件名不能为空");
        }
        if (filename.length() > DEFAULT_FILENAME_MAX_LEN) {
            throw new SysException("文件名长度超出限制");
        }
        // 检查文件拓展名
        String extension = checkExtension(file, allowedExtension);
        filename = filename + '.' + extension;
        // 保存文件
        while (baseDir.endsWith("/") || baseDir.endsWith("\\")) {
            baseDir = baseDir.substring(0, baseDir.length() - 1);
        }
        File newFile = new File(MessageFormat.format("{0}{1}{2}", baseDir, File.separator, filename));
        if (newFile.exists()) {
            throw new SysException("文件名已存在，无法保存");
        } else {
            if (!newFile.getParentFile().exists()) {
                if (!newFile.getParentFile().mkdirs()) {
                    throw new SysException("无法创建文件存储目录");
                }
            }
        }
        try {
            file.transferTo(Paths.get(newFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new SysException("文件保存失败");
        }
        return filename;
    }

    /**
     * 检查文件尺寸
     */
    public static void checkFileSize(MultipartFile file, Long fileMaxSize) {
        fileMaxSize = fileMaxSize == null || fileMaxSize < 0 ? DEFAULT_FILE_MAX_SIZE : fileMaxSize;
        if (file.getSize() > fileMaxSize) {
            throw new SysException(MessageFormat.format("文件大小为{0}KB，超出最大限制的{1}KB", (file.getSize() + 1023) / 1024, fileMaxSize));
        }
    }

    /**
     * 检查文件拓展名
     */
    public static String checkExtension(MultipartFile file, String[] allowedExtension) {
        String extension = FilenameUtils.getExtensionOfFile(file);
        boolean alowFlag = false;
        for (String allowed : allowedExtension) {
            if (allowed.equalsIgnoreCase(extension)) {
                alowFlag = true;
                break;
            }
        }
        if (!alowFlag) {
            throw new SysException("不支持上传该文件格式");
        }
        return extension;
    }

    /**
     * 用32位UUID生成文件名
     */
    public static String generateUuidFilename(String prefix, String suffix) {
        String name = UUID.randomUUID().toString().replace("-", "");
        return StringUtils.trimToNull(StringUtils.trimToEmpty(prefix) + name + StringUtils.trimToEmpty(suffix));
    }
}

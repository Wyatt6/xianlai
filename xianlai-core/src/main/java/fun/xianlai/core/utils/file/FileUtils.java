package fun.xianlai.core.utils.file;

import fun.xianlai.core.exception.SysException;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 复用org.apache.commons.io.FileUtils里的工具
 *
 * @author WyattLau
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 输出指定文件的byte数组
     */
    public static void writeBytes(String filePath, OutputStream os){
        File file = new File(filePath);
        if (!file.exists()) {
            throw new SysException("找不到文件");
        }
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int length;
            while ((length = bis.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new SysException("输出文件错误");
        } finally {
            try {
                IOUtils.close(os);
                IOUtils.close(fis);
                IOUtils.close(bis);
            } catch (IOException e) {
                throw new SysException("文件流关闭错误");
            }
        }
    }
}

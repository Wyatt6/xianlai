package fun.xianlai.basic.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author WyattLau
 */
public class ImageUtil {
    /**
     * 图像转换为base64格式的字符串
     *
     * @param image     要转换的图像
     * @param imageType 要转换的图像的类型
     * @return base64格式字符串
     */
    public static String bufferedImageToBase64(BufferedImage image, String imageType) throws IOException {
        if (imageType == null || imageType.isEmpty()) {
            imageType = "png";
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, imageType, stream);
        return "data:image/" + imageType + ";base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}

package fun.xianlai.core.utils.password;

import java.util.UUID;

/**
 * @author WyattLau
 */
public class PasswordUtils {
    // 生成加密盐
    public static String generateSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(20);
    }

    // 明文加密
    public static String encode(String plaintext, String salt) {
        return new MD5Encoder().encode(plaintext, salt);
    }
}

package fun.xianlai.basic.utils;

import java.security.MessageDigest;

/**
 * @author WyattLau
 */
public class MD5Encoder {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    // 将字节转换为16进制字符
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @param salt      加密盐
     * @return 密文
     */
    public String encode(String plaintext, String salt) {
        String ciphertext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 加密后的字符串
            ciphertext = byteArrayToHexString(md.digest(mergePasswordAndSalt(plaintext, salt).getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    // 密码明文和加密盐合并
    private String mergePasswordAndSalt(String password, String salt) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + salt;
        }
    }

    // 将byte[]转换成16进制String字符串
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(byteToHexString(b));
        }
        return result.toString();
    }
}

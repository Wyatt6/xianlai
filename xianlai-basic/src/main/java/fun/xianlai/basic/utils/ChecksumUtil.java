package fun.xianlai.basic.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author WyattLau
 */
public class ChecksumUtil {
    public static String sha256Checksum(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest(input.getBytes())) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

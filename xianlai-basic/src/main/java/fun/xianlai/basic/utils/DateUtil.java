package fun.xianlai.basic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WyattLau
 */
public class DateUtil {
    private static final String COMMON_PATTERN = "yyy-MM-dd HH:mm:ss.SSS";

    /**
     * 常用日期格式：yyy-MM-dd HH:mm:ss.SSS
     *
     * @param date 日期
     * @return 格式化日期字符串
     */
    public static String commonFormat(Date date) {
        if (date == null) return "null";
        SimpleDateFormat sdf = new SimpleDateFormat(COMMON_PATTERN);
        return sdf.format((date));
    }
}

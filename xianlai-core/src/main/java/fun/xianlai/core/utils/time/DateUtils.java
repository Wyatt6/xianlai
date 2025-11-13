package fun.xianlai.core.utils.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 复用org.apache.commons.lang3.StringUtils里的工具
 * @author WyattLau
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
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

    public static Date now() {
        return new Date();
    }

    public static Date zero() {
        return new Date(0);
    }
}

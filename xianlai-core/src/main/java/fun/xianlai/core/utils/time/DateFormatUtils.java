package fun.xianlai.core.utils.time;

import java.util.Date;

/**
 * 复用org.apache.commons.lang3.time.DateFormatUtils里的工具
 *
 * @author WyattLau
 */
public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 默认日期格式：yyy-MM-dd HH:mm:ss.SSS
     */
    public static String defaultFormat(Date date) {
        return format(date, DEFAULT_PATTERN);
    }
}

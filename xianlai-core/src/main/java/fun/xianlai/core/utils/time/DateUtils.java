package fun.xianlai.core.utils.time;

import java.util.Date;

/**
 * 复用org.apache.commons.lang3.StringUtils里的工具
 *
 * @author WyattLau
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 获取当前时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取最小时间
     */
    public static Date zero() {
        return new Date(0);
    }
}

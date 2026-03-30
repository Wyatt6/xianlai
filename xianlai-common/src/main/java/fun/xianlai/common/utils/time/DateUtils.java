package fun.xianlai.common.utils.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author WyattLau
 */
public final class DateUtils {
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HMS_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final DateTimeFormatter FORMATTER_YMD = DateTimeFormatter.ofPattern(YMD);
    public static final DateTimeFormatter FORMATTER_YMD_HMS = DateTimeFormatter.ofPattern(YMD_HMS);
    public static final DateTimeFormatter FORMATTER_YMD_HMS_MS = DateTimeFormatter.ofPattern(YMD_HMS_MS);

    /**
     * 当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 当前时间戳（毫秒级）
     */
    public static long nowMilliTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 日期格式化 yyyy-MM-dd
     */
    public static String dateFormat(LocalDate date) {
        if (date == null) return null;
        return date.format(FORMATTER_YMD);
    }

    /**
     * 时间默认格式化 yyyy-MM-dd HH:mm:ss
     */
    public static String timeDefaultFormat(LocalDateTime time) {
        if (time == null) return null;
        return time.format(FORMATTER_YMD_HMS);
    }

    /**
     * 时间毫秒格式化 yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String timeMilliFormat(LocalDateTime time) {
        if (time == null) return null;
        return time.format(FORMATTER_YMD_HMS_MS);
    }

    /**
     * 日期时间自定义格式化
     */
    public static String format(LocalDateTime time, String pattern) {
        if (time == null) return null;
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期字符串转日期对象 yyyy-MM-dd
     */
    public static LocalDate parseDate(String text) {
        if (text == null) return null;
        return LocalDate.parse(text, FORMATTER_YMD);
    }

    /**
     * 时间字符串转时间对象 yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime parseDateTime(String text) {
        if (text == null) return null;
        return LocalDateTime.parse(text, FORMATTER_YMD_HMS);
    }

    /**
     * 时间（带毫秒）字符串转时间对象 yyyy-MM-dd HH:mm:ss.SSS
     */
    public static LocalDateTime parseMilliDateTime(String text) {
        if (text == null) return null;
        return LocalDateTime.parse(text, FORMATTER_YMD_HMS_MS);
    }

    /**
     * LocalDateTime转毫秒时间戳long
     */
    public static long localDateTimeToMilliTimestamp(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * long毫秒时间戳转LocalDateTime
     */
    public static LocalDateTime milliTimstampToLocalDateTime(long milli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    /**
     * 毫秒级时间差
     */
    public static long betweenMillis(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MILLIS.between(start, end);
    }

    /**
     * 秒级时间差
     */
    public static long betweenSeconds(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.SECONDS.between(start, end);
    }

    /**
     * 分钟级时间差
     */
    public static long betweenMinutes(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * 天级别时间差
     */
    public static long betweenDays(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    private DateUtils() {}
}

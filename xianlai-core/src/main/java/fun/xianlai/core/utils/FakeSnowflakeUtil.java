package fun.xianlai.core.utils;

/**
 * 伪雪花算法
 * <p>
 * 基于雪花算法原理，缩减生成位数，生成的全局唯一ID的算法。
 * 由于前端js最大安全数为2^53-1，Long返回给前端js可能超出精度引发错误，因此这里缩减所生成的雪花ID最大位数为52位。
 * <p>
 * PS: 算法原理解析（https://juejin.cn/post/7082669476658806792）
 *
 * @author WyattLau
 */
public class FakeSnowflakeUtil {
    private static final long SEQUENCE_BITS = 10L;                      // 序列号位数
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);  // 序列号掩码
    private static final long TIMESTAMP_LSHIFT = SEQUENCE_BITS;         // 时间戳左移位数
    private static final long INIT_TIMESTAMP = 1691719990757L;          // 初始基准时间戳

    private static long sequence;      // 序列号
    private static long lastTimeMillis = -1L;

    /**
     * 获取下一个ID
     *
     * @return ID
     */
    synchronized public static long nextId() {
        long currentTimeMillis = System.currentTimeMillis();
        // 当前时间小于上次时间，可能出现服务器时钟回拨
        if (currentTimeMillis < lastTimeMillis) {
            throw new RuntimeException(String.format("服务器时钟回拨，%dms内服务不可用", lastTimeMillis - currentTimeMillis));
        }
        // 在同一毫秒内则序列号递增，否则重置序列号
        if (currentTimeMillis == lastTimeMillis) {
            // 根据掩码与运算判断序列号是否超出最大值
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimeMillis = nextMillis(lastTimeMillis);
            }
        } else {
            sequence = 0;
        }
        // 更新时间
        lastTimeMillis = currentTimeMillis;
        return ((currentTimeMillis - INIT_TIMESTAMP) << TIMESTAMP_LSHIFT) | sequence;
    }

    /**
     * 获取下一个毫秒值
     *
     * @param lastTimeMillis 上次时间
     * @return 下一个时间
     */
    private static long nextMillis(long lastTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimeMillis) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }
}

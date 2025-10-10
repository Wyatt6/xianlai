package fun.xianlai.app.common.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface PathService {
    /**
     * 缓存系统路径
     */
    void cacheSysPaths();

    /**
     * 从缓存获取系统路径
     */
    List<Map<String, String>> getSysPathsFromCache();
}

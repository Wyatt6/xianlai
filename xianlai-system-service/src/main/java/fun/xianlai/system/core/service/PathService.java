package fun.xianlai.system.core.service;

import fun.xianlai.system.core.entity.XLPath;

import java.util.List;

/**
 * @author WyattLau
 */
public interface PathService {
    /**
     * 缓存路径
     */
    void cachePaths();

    /**
     * 从缓存获取路径
     */
    List<XLPath> getPathsFromCache();
}

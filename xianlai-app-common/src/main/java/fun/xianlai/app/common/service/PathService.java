package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysPath;

import java.util.List;

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
    List<SysPath> getSysPathsFromCache();
}

package fun.xianlai.system.core.service;

import fun.xianlai.system.core.entity.XLApi;

import java.util.List;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 缓存接口
     */
    void cacheApis();

    /**
     * 从缓存获取接口
     */
    List<XLApi> getApisFromCache();
}

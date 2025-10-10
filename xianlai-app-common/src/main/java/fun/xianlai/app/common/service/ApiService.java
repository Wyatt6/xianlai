package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysApi;

import java.util.List;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 缓存系统接口
     */
    void cacheSysApis();

    /**
     * 从缓存获取系统接口
     */
    List<SysApi> getSysApisFromCache();
}

package fun.xianlai.app.common.service;

import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface OptionService {
    /**
     * 缓存加载到前端的系统参数
     */
    void cacheFrontLoadSysOptions();

    /**
     * 从缓存获取加载到前端的系统参数
     */
    Map<String, Map<String, String>> getFrontLoadSysOptionsFromCache();

    /**
     * 从缓存获取加载到前端的系统参数的checksum
     */
    String getFrontLoadSysOptionsChecksumFromCache();

    /**
     * 缓存加载到后端的系统参数缓存
     */
    void cacheBackLoadSysOptions();

    /**
     * 缓存某个加载到后端的系统参数
     */
    void cacheCertainBackLoadSysOption(String key);

    /**
     * 从缓存获取某个加载到后端的系统参数值
     */
    Optional<Object> getCertainBackLoadSysOptionValueFromCache(String key);
}

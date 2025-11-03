package fun.xianlai.app.common.service;

import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface OptionService {
    /**
     * 缓存加载到前端的参数
     */
    void cacheFrontLoadOptions();

    /**
     * 从缓存获取加载到前端的参数
     */
    Map<String, Map<String, String>> getFrontLoadOptionsFromCache();

    /**
     * 缓存加载到后端的参数缓存
     */
    void cacheBackLoadOptions();

    /**
     * 缓存某个加载到后端的参数
     */
    void cacheCertainBackLoadOption(String key);

    /**
     * 从缓存获取某个加载到后端的参数值
     */
    String getCertainBackLoadOptionValueFromCache(String key);

    /**
     * 以String类型读取参数值
     */
    Optional<String> readValueInString(String key);

    /**
     * 以Integer类型读取参数值
     */
    Optional<Integer> readValueInInteger(String key);

    /**
     * 以Long类型读取参数值
     */
    Optional<Long> readValueInLong(String key);
}

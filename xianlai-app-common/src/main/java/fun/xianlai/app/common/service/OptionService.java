package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysOption;
import fun.xianlai.core.response.DataMap;

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
     * 新增参数
     */
    DataMap add(SysOption option);

    /**
     * 删除参数
     */
    void delete(Long optionId);

    /**
     * 修改参数
     */
    DataMap edit(SysOption option);
}

package fun.xianlai.system.common.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface OptionService {
    /**
     * 更新前端加载的【系统参数】缓存
     */
    void updateFrontLoadSystemOptionsCache();

    /**
     * 更新前端加载的【租户参数】缓存
     */
    void updateFrontLoadTenantOptionsCache(Long tenantId);

    /**
     * 从缓存获取前端加载的【系统参数】参数
     */
    Map<String, Map<String, String>> getFrontLoadSystemOptionsFromCache();

    /**
     * 从缓存获取前端加载的【租户参数】参数
     */
    Map<String, Map<String, String>> getFrontLoadTenantOptionsFromCache(Long tenantId);

    /**
     * 更新后端加载的【系统参数】缓存
     */
    void updateBackLoadSystemOptionsCache();

    /**
     * 更新后端加载的【租户参数】缓存
     */
    void updateBackLoadTenantOptionsCache(Long tenantId);

    /**
     * 更新某个后端加载参数的缓存
     */
    void updateCertainBackLoadOptionCache(String key);

    /**
     * 从缓存获取某个后端加载的参数
     */
    Map<String, String> getCertainBackLoadOptionFromCache(String key);
//
//    /**
//     * 修改参数
//     */
//    DataMap edit(SysOptionDefault option);
//
//    /**
//     * 以String类型读取参数值
//     */
//    Optional<String> readValueInString(String key);
//
//    /**
//     * 以Integer类型读取参数值
//     */
//    Optional<Integer> readValueInInteger(String key);
//
//    /**
//     * 以Long类型读取参数值
//     */
//    Optional<Long> readValueInLong(String key);
}

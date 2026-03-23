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
     * 更新前端加载的【用户参数】缓存
     */
    void updateFrontLoadUserOptionsCache(Long userId);

    /**
     * 从缓存获取前端加载的【系统参数】参数
     */
    Map<String, Map<String, String>> getFrontLoadSystemOptionsFromCache();

    /**
     * 从缓存获取前端加载的【租户参数】参数
     */
    Map<String, Map<String, String>> getFrontLoadTenantOptionsFromCache(Long tenantId);

    /**
     * 从缓存获取前端加载的【用户参数】参数
     */
    Map<String, Map<String, String>> getFrontLoadUserOptionsFromCache(Long userId);

    /**
     * 更新所有后端加载的【系统参数】缓存
     */
    void updateAllBackLoadSystemOptionsCache();

    /**
     * 更新所有后端加载的【租户参数】缓存
     */
    void updateAllBackLoadTenantOptionsCache(Long tenantId);

    /**
     * 更新所有后端加载的【用户参数】缓存
     */
    void updateAllBackLoadUserOptionsCache(Long userId);

    /**
     * 更新某个后端加载参数的缓存
     */
    void updateCertainBackLoadOptionCache(String key);

    /**
     * 从缓存获取某个后端加载的参数
     */
    Map<String, String> getCertainBackLoadOptionFromCache(String key);

//    /**
//     * 新增参数
//     */
//    DataMap add(SysOption option);
//
//    /**
//     * 删除参数
//     */
//    void delete(Long optionId);
//
//    /**
//     * 修改参数
//     */
//    DataMap edit(SysOption option);

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

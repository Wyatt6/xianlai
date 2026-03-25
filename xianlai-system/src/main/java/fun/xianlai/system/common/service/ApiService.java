package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.XLApi;

import java.util.List;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 更新接口缓存
     */
    void updateApisCache();

    /**
     * 从缓存获取接口
     */
    List<XLApi> getApisFromCache();
//
//    /**
//     * 新增接口
//     */
//    DataMap add(XLApi api);
//
//    /**
//     * 删除接口
//     */
//    void delete(Long apiId);
//
//    /**
//     * 修改接口
//     */
//    DataMap edit(XLApi api);
//
//    /**
//     * 查询条件为空时查询全量数据
//     * 页码<0或页大小<=0时不分页
//     *
//     * @param pageNum   页码
//     * @param pageSize  页大小
//     * @param condition 查询条件
//     * @return 分页数据
//     */
//    Page<XLApi> getPageConditionally(int pageNum, int pageSize, XLApi condition);
}

package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.XLPath;

import java.util.List;

/**
 * @author WyattLau
 */
public interface PathService {
    /**
     * 更新路径缓存
     */
    void updatePathsCache();

    /**
     * 从缓存获取路径
     */
    List<XLPath> getPathsFromCache();
//
//    /**
//     * 新增路径
//     */
//    DataMap add(XLPath path);
//
//    /**
//     * 删除路径
//     */
//    void delete(Long pathId);
//
//    /**
//     * 修改路径
//     */
//    DataMap edit(XLPath path);
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
//    Page<XLPath> getPageConditionally(int pageNum, int pageSize, XLPath condition);
}

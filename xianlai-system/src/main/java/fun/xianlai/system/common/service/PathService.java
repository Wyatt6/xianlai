package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.Path;
import fun.xianlai.core.response.DataMap;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author WyattLau
 */
public interface PathService {
    /**
     * 缓存路径
     */
    void cachePaths();

    /**
     * 从缓存获取路径
     */
    List<Path> getPathsFromCache();

    /**
     * 新增路径
     */
    DataMap add(Path path);

    /**
     * 删除路径
     */
    void delete(Long pathId);

    /**
     * 修改路径
     */
    DataMap edit(Path path);

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<Path> getPageConditionally(int pageNum, int pageSize, Path condition);
}

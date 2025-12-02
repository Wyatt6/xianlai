package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysPath;
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
    List<SysPath> getPathsFromCache();

    /**
     * 新增路径
     */
    DataMap add(SysPath path);

    /**
     * 删除路径
     */
    void delete(Long pathId);

    /**
     * 修改路径
     */
    DataMap edit(SysPath path);

}

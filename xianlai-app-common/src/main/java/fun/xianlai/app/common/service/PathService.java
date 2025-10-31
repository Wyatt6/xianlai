package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.model.form.SysPathCondition;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author WyattLau
 */
public interface PathService {
    /**
     * 缓存系统路径
     */
    void cacheSysPaths();

    /**
     * 从缓存获取系统路径
     */
    List<SysPath> getSysPathsFromCache();

    /**
     * 条件查询路径分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<SysPath> getSysPathsByPageConditionally(int pageNum, int pageSize, SysPathCondition condition);
}

package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysApi;
import fun.xianlai.app.common.model.form.SysApiCondition;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 缓存系统接口
     */
    void cacheSysApis();

    /**
     * 从缓存获取系统接口
     */
    List<SysApi> getSysApisFromCache();

    /**
     * 条件查询接口分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 接口分页数据
     */
    Page<SysApi> getSysApisByPageConditionally(int pageNum, int pageSize, SysApiCondition condition);
}

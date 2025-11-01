package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysApi;
import fun.xianlai.app.common.model.form.SysApiCondition;
import fun.xianlai.core.response.DataMap;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author WyattLau
 */
public interface ApiService {
    /**
     * 缓存接口
     */
    void cacheApis();

    /**
     * 从缓存获取接口
     */
    List<SysApi> getApisFromCache();

    /**
     * 新增接口
     */
    DataMap add(SysApi api);

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<SysApi> getApisByPageConditionally(int pageNum, int pageSize, SysApiCondition condition);
}

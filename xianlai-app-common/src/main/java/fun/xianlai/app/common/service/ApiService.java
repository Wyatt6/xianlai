package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysApi;
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
     * 删除接口
     */
    void delete(Long apiId);

    /**
     * 修改接口
     */
    DataMap edit(SysApi api);
}

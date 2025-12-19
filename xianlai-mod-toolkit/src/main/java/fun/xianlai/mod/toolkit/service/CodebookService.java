package fun.xianlai.mod.toolkit.service;

import fun.xianlai.core.response.DataMap;
import fun.xianlai.mod.toolkit.model.entity.SecretCode;
import org.springframework.data.domain.Page;

/**
 * @author WyattLau
 */
public interface CodebookService {
    /**
     * 新增密码条目
     */
    DataMap add(SecretCode secretCode);

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<SecretCode> getPageConditionally(int pageNum, int pageSize, SecretCode condition);
}

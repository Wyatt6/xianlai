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
     * 新增路径
     */
    DataMap add(SysPath path);
}

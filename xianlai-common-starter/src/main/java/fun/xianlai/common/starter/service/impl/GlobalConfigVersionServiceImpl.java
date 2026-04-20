package fun.xianlai.common.starter.service.impl;

import fun.xianlai.common.exception.SysException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.starter.service.GlobalConfigVersionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 全局配置版本号服务
 *
 * @author WyattLau
 */
@Slf4j
public class GlobalConfigVersionServiceImpl implements GlobalConfigVersionService {
    /**
     * 全局配置版本号（线程安全）
     */
    private final AtomicLong version = new AtomicLong(0);

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * 初始化全局配置版本号
     */
    @PostConstruct
    public void initVersion() {
        try {
            version.set(this.getVersionFromDb());
            log.info("全局配置版本号已加载: {}", version.get());
        } catch (Exception e) {
            throw new SysException(RetCode.INIT_ERROR, "全局配置版本号无法加载: " + e.getMessage());
        }
    }

    @Override
    public void pollVersionTask() {
        try {
            Long versionFromDb = this.getVersionFromDb();
            if (versionFromDb != null && !versionFromDb.equals(version.get())) {
                log.info("全局配置版本号更新: {} -> {}", version.get(), versionFromDb);
                version.set(versionFromDb);
            }
        } catch (Exception e) {
            log.warn("全局配置版本号同步异常，使用本地内存版本号，异常信息: {}", e.getMessage());
        }
    }

    private Long getVersionFromDb() {
        return jdbc.queryForObject("select version from tb_core_global_config_version limit 1", Long.class);
    }

    @Override
    public Long getLocalVersion() {
        return version.get();
    }
}

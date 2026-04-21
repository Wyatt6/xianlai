package fun.xianlai.common.starter.service;

/**
 * @author WyattLau
 */
public interface ConfigService {
    /**
     * 全局配置轮询任务（包括版本和配置数据）
     */
    void globalConfigPollingTask();
}

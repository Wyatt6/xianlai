package fun.xianlai.common.starter.service;

/**
 * @author WyattLau
 */
public interface GlobalConfigVersionService {
    /**
     * 全局配置版本号轮询任务
     */
    void pollVersionTask();

    /**
     * 获取本地内存的全局配置版本号
     */
    Long getLocalVersion();
}

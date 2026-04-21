package fun.xianlai.common.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author WyattLau
 */
@Configuration
public class TaskConfig {
    /**
     * 创建全局定时任务调度器
     * 这里用的是Spring Task，专用于轮询、心跳、清理等系统底层任务，业务的定时任务用另外的Quartz
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("sys-task-");
        scheduler.setPoolSize(10);
        return scheduler;
    }
}

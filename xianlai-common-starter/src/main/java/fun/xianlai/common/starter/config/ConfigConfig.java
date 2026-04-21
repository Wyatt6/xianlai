package fun.xianlai.common.starter.config;

import fun.xianlai.common.starter.properties.ConfigProperties;
import fun.xianlai.common.starter.service.ConfigService;
import fun.xianlai.common.starter.service.impl.ConfigServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Duration;

/**
 * @author WyattLau
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "xianlai.config", name = "enabled", havingValue = "true")
public class ConfigConfig {
    /**
     * 配置服务
     */
    @Bean
    public ConfigService configService() {
        return new ConfigServiceImpl();
    }

    /**
     * 注册全局配置定时轮询任务
     */
    @Bean
    public ScheduledTaskRegistrar globalConfigPollingTaskRegistrar(
            ThreadPoolTaskScheduler scheduler,
            ConfigService configService,
            ConfigProperties properties) {
        ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
        registrar.setTaskScheduler(scheduler);
        registrar.addFixedRateTask(configService::globalConfigPollingTask, Duration.ofMillis(properties.getGlobalConfigPollRate()));
        return registrar;
    }
}

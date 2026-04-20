package fun.xianlai.common.starter.config;

import fun.xianlai.common.starter.properties.ConfigProperties;
import fun.xianlai.common.starter.service.ConfigService;
import fun.xianlai.common.starter.service.GlobalConfigVersionService;
import fun.xianlai.common.starter.service.impl.ConfigServiceImpl;
import fun.xianlai.common.starter.service.impl.GlobalConfigVersionServiceImpl;
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
     * 全局配置版本号服务
     */
    @Bean
    public GlobalConfigVersionService globalConfigVersionService() {
        return new GlobalConfigVersionServiceImpl();
    }

    /**
     * 配置服务
     */
    @Bean
    public ConfigService configService() {
        return new ConfigServiceImpl();
    }

    /**
     * 注册全局配置版本号定时轮询任务
     */
    @Bean
    public ScheduledTaskRegistrar pollVersionTaskRegistrar(
            ThreadPoolTaskScheduler scheduler,
            GlobalConfigVersionService versionService,
            ConfigProperties properties) {
        ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
        registrar.setTaskScheduler(scheduler);
        registrar.addFixedRateTask(versionService::pollVersionTask, Duration.ofMillis(properties.getGlobalConfigVersionPollRate()));
        return registrar;
    }
}

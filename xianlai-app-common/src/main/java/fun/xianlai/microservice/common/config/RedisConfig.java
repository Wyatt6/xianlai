package fun.xianlai.microservice.common.config;

import fun.xianlai.microservice.common.properties.LettucePoolProperties;
import fun.xianlai.microservice.common.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author WyattLau
 */
@EnableCaching
@Configuration
public class RedisConfig {
    @Autowired
    private RedisProperties rp;
    @Autowired
    private LettucePoolProperties lpp;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(rp.getHost());
        config.setPort(rp.getPort());
        config.setPassword(rp.getPassword());
        config.setDatabase(rp.getDatabase());
        LettucePoolingClientConfiguration poolConfig = LettucePoolingClientConfiguration.builder().build();
        poolConfig.getPoolConfig().setMinIdle(lpp.getMinIdle());
        poolConfig.getPoolConfig().setMaxIdle(lpp.getMaxIdle());
        poolConfig.getPoolConfig().setMaxTotal(lpp.getMaxTotal());
        poolConfig.getPoolConfig().setMaxWait(Duration.ofMillis(lpp.getMaxWait()));
        return new LettuceConnectionFactory(config, poolConfig);
    }

    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

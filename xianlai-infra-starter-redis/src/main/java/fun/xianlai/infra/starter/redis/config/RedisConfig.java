package fun.xianlai.infra.starter.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import fun.xianlai.infra.starter.redis.properties.LettucePoolProperties;
import fun.xianlai.infra.starter.redis.properties.RedisProperties;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Jackson配置（解决LocalDateTime格式问题）
     */
    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册 JSR310 时间模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 自定义序列化与反序列化规则
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        objectMapper.registerModule(javaTimeModule);
        // 关闭时间转时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // *注：解决缓存数据没有全类名，重启后从缓存读取数据时不知道反序列化为哪个类
        // 支持范型的序列化，序列化时携带全类名，保证能反序列化为原类，而不变成LinkedHashMap无法类型转换为目标类型
         objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(getObjectMapper());
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}

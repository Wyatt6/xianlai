package fun.xianlai.pkg.redis.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import fun.xianlai.pkg.redis.properties.LettucePoolProperties;
import fun.xianlai.pkg.redis.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
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
    /**
     * Fastjson2 序列化器
     */
    private static final RedisSerializer<Object> FASTJSON2_SERIALIZER = new RedisSerializer<Object>() {
        @Override
        public byte[] serialize(Object value) {
            if (value == null) return new byte[0];
            return JSON.toJSONBytes(value,
                    JSONWriter.Feature.WriteClassName,
                    JSONWriter.Feature.FieldBased,
                    JSONWriter.Feature.WriteMapNullValue
            );
        }

        @Override
        public Object deserialize(byte[] bytes) {
            if (bytes == null || bytes.length == 0) return null;
            return JSON.parseObject(bytes, Object.class,
                    JSONReader.Feature.SupportClassForName,
                    JSONReader.Feature.FieldBased
            );
        }
    };

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
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(FASTJSON2_SERIALIZER);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(FASTJSON2_SERIALIZER);
        return template;
    }
}

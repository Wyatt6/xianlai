package fun.xianlai.microservice.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import fun.xianlai.microservice.common.properties.DruidPoolProperties;
import fun.xianlai.microservice.common.properties.MysqlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author WyattLau
 */
@Slf4j
@Configuration
public class DataSourceConfig {
    // 使用 Unicode 字符集
    // 字符编码 utf8
    // 不使用 SSL 连接
    // 允许返回数据库生成的主键
    // 时区东八区
    private static final String url = "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";

    @Autowired
    private MysqlProperties mp;
    @Autowired
    private DruidPoolProperties dpp;

    @Bean
    public DataSource druidDateSource() {
        DruidDataSource ds = new DruidDataSource();
        // 数据库连接基础配置
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(MessageFormat.format(url, mp.getHost(), mp.getPort(), mp.getDb()));
        ds.setUsername(mp.getUsername());
        ds.setPassword(mp.getPassword());
        // Druid连接池配置
        ds.setInitialSize(dpp.getInitialSize());
        ds.setMinIdle(dpp.getMinIdle());
        ds.setMaxActive(dpp.getMaxActive());
        ds.setMaxWait(dpp.getMaxWait());
        ds.setMinEvictableIdleTimeMillis(dpp.getMinEvictableIdleTimeMillis());
        ds.setTimeBetweenEvictionRunsMillis(dpp.getTimeBetweenEvictionRunsMillis());
        ds.setTestWhileIdle(dpp.getTestWhileIdle());
        ds.setTestOnBorrow(dpp.getTestOnBorrow());
        ds.setTestOnReturn(dpp.getTestOnReturn());
        ds.setValidationQuery(dpp.getValidationQuery());
        ds.setPoolPreparedStatements(dpp.getPoolPreparedStatements());
        ds.setMaxPoolPreparedStatementPerConnectionSize(dpp.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            ds.setFilters(dpp.getFilters());
            Properties connectProperties = new Properties();
            connectProperties.put("config.decrypt", "true");    // 这里必须使用字符串
            connectProperties.put("config.decrypt.key", mp.getPublicKey());
            ds.setConnectProperties(connectProperties);
            ds.init();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return ds;
    }
}

package fun.xianlai.app.iam.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("datasource.mysql.druid.pool")
public class DruidPoolProperties {
    // 详见：https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
    // 或：https://zhuanlan.zhihu.com/p/590960572
    private int initialSize = 1;                                // 初始连接数
    private int minIdle = 1;                                    // 最小空闲连接数
    private int maxActive = 100;                                // 最大活跃连接数
    private long maxWait = 60000L;                              // 获取连接的最大等待时间（ms）
    private long minEvictableIdleTimeMillis = 300000L;          // 连接在池中最小生存时间（ms）
    private long timeBetweenEvictionRunsMillis = 60000L;        // 前后两次检测可关闭空闲连接的间隔时间（ms）
    private Boolean testWhileIdle = true;                       // 空闲时验证连接有效性（不影响性能）
    private Boolean testOnBorrow = false;                       // 申请连接时检测连接有效性（影响性能，建议关闭）
    private Boolean testOnReturn = false;                       // 归还连接时检测连接有效性（影响性能，建议关闭）
    private String validationQuery = "select 'x'";              // 检测连接有效性的查询SQL
    private Boolean poolPreparedStatements = false;             // 启用PSCache（对支持游标的数据库性能提升巨大），Oracle建议开启，MySQL建议关闭
    private int maxPoolPreparedStatementPerConnectionSize = 50; // 启用PSCache时有效，每个连接最多持有的PACache数量
    private String filters = "config,stat,wall,log4j";          // 扩展插件
}

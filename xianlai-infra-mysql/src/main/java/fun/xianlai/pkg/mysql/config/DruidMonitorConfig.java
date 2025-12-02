package fun.xianlai.pkg.mysql.config;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import fun.xianlai.pkg.mysql.properties.DruidMonitorProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 访问地址：http://ip:port/druid/index.html
 * 配置参考网址：https://www.springcloud.io/post/2022-02/spring-boot-druid-monitors/
 *
 * @author WyattLau
 */
@Configuration
public class DruidMonitorConfig {
    @Autowired
    private DruidMonitorProperties dmp;

    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<Servlet>(new StatViewServlet(), "/druid/*");
        // 登录Druid监控后台的账号密码
        bean.addInitParameter("loginUsername", dmp.getUsername());
        bean.addInitParameter("loginPassword", dmp.getPassword());
        // 黑白名单
        bean.addInitParameter("allow", dmp.getWhiteList());
        bean.addInitParameter("deny", dmp.getBlackList());   // 比白名单更高优先级
        // 重置数据功能
        bean.addInitParameter("resetEnable", "true");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> webStatFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>();
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        // Session监控配置
        bean.addInitParameter("sessionStatEnable", "true");
        bean.addInitParameter("sessionStatMaxCount", "1000");
        return bean;
    }
}

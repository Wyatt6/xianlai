package fun.xianlai.app.iam.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WyattLau
 */
@Configuration
public class SatokenConfig implements WebMvcConfigurer {
    private static final long DEFAULT_TIMEOUT = 43200;          // 默认token有效期：12小时
    private static final long DEFAULT_ACTIVE_TIMEOUT = 3600;    // 默认token临时有效期1小时，指定时间内无操作会冻结

    // Sa-Token 参数配置，参考文档：https://sa-token.cc
    // 此配置会与 application.yml 中的配置合并 （代码配置优先）
    @Autowired
    public void configSaToken(SaTokenConfig config) {
        config.setTokenName("token");                       // token名称（同时也是cookie名称）
        config.setTimeout(DEFAULT_TIMEOUT);                 // token有效期（单位：秒），-1代表永不过期
        config.setActiveTimeout(DEFAULT_ACTIVE_TIMEOUT);    // token临时有效期（指定时间内无操作就视为token过期），-1代表永不过期
        config.setIsConcurrent(false);                      // 是否允许同一账号多地同时登录（为true时允许一起登录，为false时新登录挤掉旧登录）
        config.setIsShare(false);                           // 在多人登录同一账号时，是否共用一个token（为true时所有登录共用一个token，为false时每次登录新建一个token）
        config.setTokenStyle("simple-uuid");                // token风格
        config.setIsLog(false);                             // 是否输出操作日志
        config.setIsPrint(false);                           // 是否在初始化配置时打印版本字符画
        config.setIsReadCookie(false);                      // 是否尝试从Cookie里读取token，此值为false后，StpUtil.login(id)登录时也不会再往前端注入Cookie
    }

    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }
}

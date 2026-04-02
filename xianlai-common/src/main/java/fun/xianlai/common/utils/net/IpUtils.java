package fun.xianlai.common.utils.net;


import jakarta.servlet.http.HttpServletRequest;

/**
 * @author WyattLau
 */
public final class IpUtils {
    /**
     * 适用于Web，不适用于WebFlux
     * 获取真实IP（处理Nginx或其他代理组件转发的报文）
     * Nginx配置要加上：
     * proxy_set_header Host $host;
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     */
    public static String getRemoteIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        // 反向代理会把真实IP放在这些请求头
        String ip = request.getHeader("X-Forwarded-For");                   // 国际标准，必须配置
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Real-IP");                            // Nginx专用，常用
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");                      // Apache专用
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");                   // WebLogic专用
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();                                         // 兜底
        }

        if (ip == null) {
            return "unknown";
        }
        // 多级代理取第一个
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        // IPV6 本地转 IPV4
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private IpUtils() {
    }
}

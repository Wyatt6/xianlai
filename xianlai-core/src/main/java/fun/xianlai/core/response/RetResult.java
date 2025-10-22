package fun.xianlai.core.response;

import fun.xianlai.core.exception.SysException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于后端向前端返回响应数据，或服务生产者向服务消费者返回处理结果
 *
 * @author WyattLau
 */
@Data
public class RetResult {
    private boolean success;                // 处理结果：true-成功 / false-失败
    private DataMap data = new DataMap();   // 返回数据：以键值对形式存储
    private String traceId;                 // 跟踪标记：网关接收请求就已经生成的全局ID，用于全链路跟踪交易

    public RetResult() {
        this.success = false;   // 出于安全考量，构造函数中默认设置success标记为false（失败）
    }

    public RetResult success() {
        this.success = true;
        return this;
    }

    public RetResult fail() {
        this.success = false;
        return this;
    }

    /**
     * Lombok的@Getter给boolean生成的是isXXX()这样的方法，给Boolean生成的是getXXX()这样的方法
     * 这时候fastjson就无法正确解析json字符串中的布尔值，因为它找的是getXXX()这样的方法
     */
    public boolean getSuccess() {
        return this.success;
    }

    public RetResult setFailCode(String code) {
        this.data.put("failCode", code);
        return this;
    }

    public RetResult setFailMessage(String message) {
        this.data.put("failMessage", message);
        return this;
    }

    public RetResult addChecksum(String key, String checksum) {
        if (!this.data.containsKey("checksum")) {
            this.data.put("checksum", new HashMap<String, String>());
        }
        ((Map<String, String>) this.data.get("checksum")).put(key, checksum);
        return this;
    }

    public RetResult setData(DataMap data) {
        this.data = data;
        return this;
    }

    public RetResult addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public RetResult writeFeignData(Object value) {
        this.success().addData("retResult", value);
        return this;
    }

    public Object readFeignData() {
        return this.data.get("retResult");
    }

    public RetResult writeFeignSysException(SysException e) {
        this.fail().addData("sysException", e.getMessage());
        return this;
    }

    public SysException readFeignSysException() {
        return new SysException((String) this.data.get("sysException"));
    }

    public RetResult setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}

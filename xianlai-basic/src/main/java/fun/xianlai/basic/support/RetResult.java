package fun.xianlai.basic.support;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WyattLau
 */
@Getter
public class RetResult {
    private Boolean success;                // true - 成功 / false - 失败
    private DataMap data;                   // 返回数据
    private String traceId;                 // 日志跟踪ID

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

    public RetResult setFailCode(String code) {
        if (this.data == null) {
            this.data = new DataMap("failCode", code);
        } else {
            this.data.put("failCode", code);
        }
        return this;
    }

    public RetResult setFailMessage(String message) {
        if (this.data == null) {
            this.data = new DataMap("failMessage", message);
        } else {
            this.data.put("failMessage", message);
        }
        return this;
    }

    public RetResult addChecksum(String key, String checksum) {
        if (this.data == null) {
            this.data = new DataMap();
        }
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
        if (this.data == null) {
            this.data = new DataMap(key, value);
        } else {
            this.data.put(key, value);
        }
        return this;
    }

    public RetResult setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}

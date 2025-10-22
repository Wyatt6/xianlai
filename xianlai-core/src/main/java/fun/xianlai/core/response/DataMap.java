package fun.xianlai.core.response;

import java.util.HashMap;

/**
 * 键值对数据类型封装对象
 *
 * @author WyattLau
 */
public class DataMap extends HashMap<String, Object> {
    public DataMap() {
        super();
    }

    public DataMap(String key, Object value) {
        super();
        this.put(key, value);
    }
}

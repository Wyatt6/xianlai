package fun.xianlai.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用于后端向前端返回响应数据，或服务生产者向服务消费者返回处理结果
 *
 * @author WyattLau
 */
@NoArgsConstructor
@Getter
public class RetResult<T> {
    private boolean success;                // 处理结果：true-成功 / false-失败
    private String code;                    // 响应状态码
    private String message;                 // 响应提示消息
    private T data;                         // 返回数据
    private String traceId;                 // 跟踪标记：网关接收请求就已经生成的全局ID，用于全链路跟踪交易
    private final long timestamp = System.currentTimeMillis();  // 响应时间戳（毫秒级）

    /**
     * 成功
     * 无数据，默认提示消息
     */
    public static <T> RetResult<T> success() {
        return new RetResult<T>()
                .setSuccess(true)
                .setCode(RetCode.SUCCESS)
                .setMessage("操作成功")
                .setData(null);
    }

    /**
     * 成功
     * 业务数据+默认提示消息
     */
    public static <T> RetResult<T> success(T data) {
        return new RetResult<T>()
                .setSuccess(true)
                .setCode(RetCode.SUCCESS)
                .setMessage("操作成功")
                .setData(data);
    }

    /**
     * 成功
     * 业务数据+自定义提示消息
     */
    public static <T> RetResult<T> success(String message, T data) {
        return new RetResult<T>()
                .setSuccess(true)
                .setCode(RetCode.SUCCESS)
                .setMessage(message)
                .setData(data);
    }



    /**
     * 失败
     * 通用错误码+默认提示消息
     */
    public static <T> RetResult<T> fail() {
        return new RetResult<T>()
                .setSuccess(false)
                .setCode(RetCode.BIZ_ERROR)
                .setMessage("操作失败")
                .setData(null);
    }

    /**
     * 失败
     * 自定义错误码+自定义提示消息
     */
    public static <T> RetResult<T> fail(String code, String message) {
        return new RetResult<T>()
                .setSuccess(false)
                .setCode(code)
                .setMessage(message)
                .setData(null);
    }

    // ----- getters and getters -----

    public RetResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * Lombok给boolean生成的getter是isXXX()形式的
     * Fastjson2默认反射机制使用的getter是getXXX()形式的
     */
    public boolean getSuccess() {
        return this.success;
    }

    public RetResult<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public RetResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public RetResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public RetResult<T> setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

//    public RetResult addData(String key, Object value) {
//        this.data.put(key, value);
//        return this;
//    }
//
//    public RetResult addExtraData(String key, Object extraData) {
//        if (!this.data.containsKey("extraData")) {
//            this.data.put("extraData", new HashMap<String, Object>());
//        }
//        BeanUtils.objectToMap(this.data.get("extraData")).put(key, extraData);
//        return this;
//    }
//
//    public RetResult writeFeignData(Object value) {
//        this.success().addData("retResult", value);
//        return this;
//    }
//
//    public Object readFeignData() {
//        return this.data.get("retResult");
//    }
//
//    public RetResult writeFeignSysException(SysException e) {
//        this.fail().addData("sysException", e.getMessage());
//        return this;
//    }
//
//    public SysException readFeignSysException() {
//        return new SysException((String) this.data.get("sysException"));
//    }
}

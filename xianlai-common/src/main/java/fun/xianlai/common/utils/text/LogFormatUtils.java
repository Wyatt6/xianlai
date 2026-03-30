package fun.xianlai.common.utils.text;

/**
 * @author WyattLau
 */
public class LogFormatUtils {
    /**
     * 参数日志打印器
     * 美化打印请求参数：每个参数一行
     *
     * @param args          要打印的参数列表
     * @return              组装好的打印字符串
     */
    public static String paramLoggingFormatter(Object[] args) {
        if (args == null || args.length == 0) {
            return "none";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                sb.append("\n  ").append(i).append(" -> null");
                continue;
            }
            if (isSkipObject(arg)) {
                sb.append("\n  ").append(i).append(" -> ")
                        .append(arg.getClass().getSimpleName()).append(" [skip]");
                continue;
            }
            sb.append("\n  ").append(i).append(" -> ").append(arg);
        }
        return sb.toString();
    }

    /**
     * 跳过 request/response/file 等
     */
    private static boolean isSkipObject(Object arg) {
        String name = arg.getClass().getName();
        return name.contains("HttpServletRequest")
                || name.contains("HttpServletResponse")
                || name.contains("MultipartFile")
                || name.contains("Model")
                || name.contains("RedirectAttributes");
    }
}

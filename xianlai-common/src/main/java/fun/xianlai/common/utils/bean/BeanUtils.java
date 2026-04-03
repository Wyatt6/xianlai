package fun.xianlai.common.utils.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 通用对象操作工具
 *
 * @author WyattLau
 */
@Slf4j
public final class BeanUtils {
    /**
     * 安全地将bean转为Map<String, Object>
     * 支持父类属性的获取
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) return map;
        // bean原本就是Map类型的
        if (obj instanceof Map<?, ?>) {
            ((Map<?, ?>) obj).forEach((k, v) -> map.put(String.valueOf(k), v));
            return map;
        }
        // bean原本不是Map类型的，需要遍历其继承的所有父类字段
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);  // bean里所有属性定义都是private的，不强制访问转不出来
                try {
                    map.put(field.getName(), field.get(obj));
                } catch (Exception ignored) {
                    // 单个字段获取失败，跳过，不影响整体
                }
            }
        }
        return map;
    }


    /**
     * 对象解析成Long
     */
    public static Long parseLong(Object obj) {
        return Optional.ofNullable(obj)
                .map(Object::toString)
                .map(Long::valueOf)
                .orElse(null);
    }

    /**
     * 从obj对象中获取名称为fieldName的属性值
     * 如果obj为null时直接返回null
     * 支持其父类的属性
     */
    public static <T> T getFieldValue(Object obj, String fieldName, Class<T> clazz) {
        if (obj == null) return null;

        for (Class<?> clazzType = obj.getClass(); clazzType != Object.class; clazzType = clazzType.getSuperclass()) {
            for (Field field : clazzType.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    try {
                        return clazz.cast(field.get(obj));
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        log.warn("获取属性值失败: {}.{}", obj.getClass().getName(), fieldName, e);
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 非空复制
     * 当source中的属性不为null时，复制给target对应的属性
     * 注意：暂无法处理父类属性
     */
    public static void copyPropertiesNotNull(Object source, Object target) throws BeansException {
        if (source == null || target == null) return;

        Class<?> sourceClazz = source.getClass();
        Class<?> targetClazz = target.getClass();

        for (; sourceClazz != Object.class; sourceClazz = sourceClazz.getSuperclass()) {
            for (Field field : sourceClazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(source);
                    if (value == null) continue;
                    Field targetField = ReflectionUtils.findField(targetClazz, field.getName());
                    if (targetField == null) continue;
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * trim bean中所有String类型的属性
     * 支持对父类String属性的trim
     */
    public static void trimString(Object obj) {
        if (obj == null) return;

        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    try {
                        String value = (String) field.get(obj);
                        if (value != null) {
                            field.set(obj, value.trim());
                        }
                    } catch (Exception ignored) {
                        // 跳过，不影响整体
                    }
                }
            }
        }
    }

    private BeanUtils() {}
}

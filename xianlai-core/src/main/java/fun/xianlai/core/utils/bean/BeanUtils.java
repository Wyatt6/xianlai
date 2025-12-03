package fun.xianlai.core.utils.bean;

import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.StringUtils;
import org.springframework.beans.BeansException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 复用org.springframework.beans.BeanUtils里的工具
 *
 * @author WyattLau
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
    /**
     * 从obj对象中获取名称为fieldName的属性值
     * 如果obj为null时直接返回null
     */
    public static <T> T getFieldValue(Object obj, String fieldName, Class<T> clazz) {
        if (obj == null) return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                try {
                    // boolean类型的getter要特殊处理，因为lombok生成的是isXXX
                    String getterName = (boolean.class.getName().equals(clazz.getName()) ? "is" : "get")
                            + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getter = obj.getClass().getMethod(getterName);
                    Object value = getter.invoke(obj);
                    return value == null ? null : (T) value;
                } catch (NoSuchMethodException e) {
                    throw new SysException("无法找到getter");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new SysException("调用失败");
                }
            }
        }
        return null;
    }

    /**
     * 当source中的属性不为null时，复制给target对应的属性
     * 注意：暂无法处理父类属性
     */
    public static void copyPropertiesNotNull(Object source, Object target) throws BeansException {
        Field[] sourceFields = source.getClass().getDeclaredFields();   // 不包含父类属性
        for (Field sourceField : sourceFields) {
            try {
                String fieldName = sourceField.getName();
                Field targetField = target.getClass().getDeclaredField(fieldName);

                String sourceGetterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // boolean类型的getter要特殊处理，因为lombok生成的是isXXX
                if (boolean.class.getName().equals(sourceField.getType().getName())) {
                    sourceGetterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                }
                Method sourceGetter = source.getClass().getMethod(sourceGetterName);

                String targetSetterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method targetSetter = target.getClass().getMethod(targetSetterName, targetField.getType());

                Object value = sourceGetter.invoke(source);
                if (value != null) {
                    targetSetter.invoke(target, value);
                }
            } catch (NoSuchFieldException ignored) {
            } catch (NoSuchMethodException e) {
                throw new SysException("无法找到getter/setter");
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new SysException("调用失败");
            }
        }
    }

    /**
     * trim对象中所有String类型的属性
     * 注意：暂无法处理父类中定义的属性
     */
    public static void trimString(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (String.class.getName().equals(field.getType().getName())) {
                try {
                    String fieldName = field.getName();
                    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getter = obj.getClass().getMethod(getterName);
                    Method setter = obj.getClass().getMethod(setterName, field.getType());
                    String value = (String) getter.invoke(obj);
                    setter.invoke(obj, StringUtils.trim(value));
                } catch (NoSuchMethodException e) {
                    throw new SysException("无法找到getter/setter");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new SysException("调用失败");
                }
            }
        }
    }
}

package fun.xianlai.core.utils;

import fun.xianlai.core.exception.SysException;
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
}

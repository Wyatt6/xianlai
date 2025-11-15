package fun.xianlai.core.utils;

import fun.xianlai.core.exception.SysException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author WyattLau
 */
public class EntityUtil {
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

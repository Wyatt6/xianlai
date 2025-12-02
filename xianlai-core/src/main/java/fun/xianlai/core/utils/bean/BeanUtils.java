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
